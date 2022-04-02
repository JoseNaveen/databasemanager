package com.joey.databasemanager.nse;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joey.databasemanager.dto.CE;
import com.joey.databasemanager.dto.DataContent;
import com.joey.databasemanager.dto.NSEContent;
import com.joey.databasemanager.dto.OptionChain;
import com.joey.databasemanager.repository.nosql.OptionChainNosqlRepository;


@SpringBootTest
public class OptionsTests {
	
	@Autowired
	private OptionChainNosqlRepository optionRepo;
	
	@Value("${nse.cookie}")
	private String cookie;
	
	/*
	 * @Value("${nse.stock_symbols}") private List<String> stocks;
	 */
	
	@Value("${trade_mode.both}")
	private boolean both;
	
	@Value("${trade_mode.low_side}")
	private boolean low;
	
	@Value("${trade_mode.high_side}")
	private boolean high;
	
	@Value("#{${nse.stock_lot_size}}")
	private Map<String,Integer> lotSize;
	
	private HttpClient client = HttpClient.newHttpClient();
	
	public class PossibleTrade {
		
		public PossibleTrade(CE itmCall, CE otmCall, CE atmCall) {
			this.itmCall = itmCall;
			this.otmCall = otmCall;
			this.atmCall = atmCall;
		}
		final private CE itmCall;
		final private CE otmCall;
		final private CE atmCall;
		
		public String toString() {
			StringBuilder sb = new StringBuilder();
			
			return sb.toString();
		}
		
		
	}
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(OptionsTests.class);
	
	
	@Test
	public void test1() throws Exception {
		
		log.info("started");
		List<NSEContent> list = lotSize.keySet().stream().map(item-> {
			try {
				return getDetails(item);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}).collect(Collectors.toList());
		
		for(NSEContent i: list) {
			CE stock_ce = i.getRecords().getData().stream().filter(d-> {
				return d.getCE()!=null;
			}).map(item-> {
				return item.getCE();
			}).findFirst().orElseThrow();
			OptionChain optionchain = new OptionChain();
			optionchain.setNsecontent(i);
			optionchain.setStock_name(stock_ce.getUnderlying());
			optionRepo.insert(optionchain);
		}
		
		Map<String,Map<String,CE>> atmTrades = new HashMap<>();
		Map<String,List<CE>> otmTrades = new HashMap<>();
		Map<String,List<CE>> itmTrades = new HashMap<>();
		for(NSEContent c: list) {
			Float cmp = c.getRecords().getUnderlyingValue();
			Float current_div = null;
			CE ce = null;
			DataContent dcontent;
			dcontent = c.getRecords().getData().stream().filter(item->{
				return item.getCE()!=null;
			}).findFirst().get();
			String stock_name = dcontent.getCE().getUnderlying();
			log.info("Stock_name: " + stock_name + " cmp: "+ cmp);
			List<String> expiryDates = c.getRecords().getExpiryDates();
			Map<String,CE> atmExpiryTrades = new HashMap<>();
			atmTrades.put(stock_name,atmExpiryTrades);
			for(String expiryDate: expiryDates) {
				//log.info("Processing expiry: " + expiryDate);
				List<DataContent> tradesAtCurExpiry = c.getRecords().getData().stream()
				.filter(item-> {
					return item.getExpiryDate().equals(expiryDate);
				})
				.filter(item-> {
					return item.getCE()!=null && item.getCE().getOpenInterest()>0;
				})
				.collect(Collectors.toList());
				if(tradesAtCurExpiry.isEmpty()) {
					log.info("tradesAtCurExpiry is empty");
					continue;
				}
				for(DataContent d: tradesAtCurExpiry) {
					stock_name = d.getCE().getUnderlying();
					//log.info("processing current expiry: "+ stock_name);
					if(current_div==null) {
						current_div = Math.abs(cmp-d.getCE().getStrikePrice());
						continue;
					}
					if (current_div > Math.abs(cmp-d.getCE().getStrikePrice())) {
						current_div = Math.abs(cmp-d.getCE().getStrikePrice());
						ce = d.getCE();
						//log.info("setting atm to: " + ce.getStrikePrice());
					}	
				}
				if(ce!=null) {
					atmTrades.get(stock_name).put(expiryDate, ce);
					ce = null;
					current_div = null;
				}
				else {
					//log.info("ce is null");
				}
				
			}
			atmTrades.forEach((s,m)-> {
				//log.info(s + String.valueOf(m.size()));
			});
			//log.info("underlying: " + stock_name + " cmp: " + ce.getUnderlyingValue() + " atm call strike: " + ce.getStrikePrice());
		}
		
		for(NSEContent c: list) {
			String stock_name = null;
			
			List<CE> itmCalls = getITMCall(c.getRecords().getData());
			stock_name = itmCalls.get(0).getUnderlying();
			itmTrades.put(stock_name, itmCalls);
		}
		
		for(NSEContent c: list) {
			String stock_name = null;
			
			List<CE> otmCalls = getOTMCall(c.getRecords().getData());
			stock_name = otmCalls.get(0).getUnderlying();
			otmTrades.put(stock_name, otmCalls);
		}
		
		for(String entry: atmTrades.keySet()) {
			//log.info("processing symbol: " + entry);
			Map<String, CE> expiryMap = atmTrades.get(entry);
			for(Entry<String, CE> expiryEntry: expiryMap.entrySet()) {
				//log.info("processing expiry: " + expiryEntry.getKey());
				List<CE> expiryItmTradesList = itmTrades.get(entry).stream().filter(item-> {
					return item.getExpiryDate().equals(expiryEntry.getKey());
				})
				.collect(Collectors.toList());
				
				List<CE> expiryOtmTradesList = otmTrades.get(entry).stream().filter(item-> {
					return item.getExpiryDate().equals(expiryEntry.getKey());
				})
				.collect(Collectors.toList());
				for(CE ceitm: expiryItmTradesList) {
					for(CE ceotm: expiryOtmTradesList) {
						float itmltp = ceitm.getLastPrice();
						float otmltp = ceotm.getLastPrice();
						float atmltp = expiryEntry.getValue().getLastPrice();
						
						float itmbreakeven = ceitm.getStrikePrice() + ceitm.getLastPrice();
						float otmbreakeven = ceotm.getStrikePrice() + ceotm.getLastPrice();
						float atmbreakeven = expiryEntry.getValue().getStrikePrice() + expiryEntry.getValue().getLastPrice();
						
						if(itmltp+otmltp<2*atmltp && ((itmbreakeven + otmbreakeven)/2 <atmbreakeven) && both) {
								//log.info("itmbreakeven: " + itmbreakeven + " " + ceitm.getStrikePrice());
								//log.info("otmbreakeven: " + otmbreakeven + " " + ceotm.getStrikePrice());
								//log.info("average: " + (itmbreakeven+otmbreakeven)/2 + " atmbreakeven: " + atmbreakeven);
								log.info("possible trade: " + entry + " itmCall: " + ceitm.getStrikePrice() + " oi: "+ ceitm.getOpenInterest()
								+ " otmCall: " + ceotm.getStrikePrice() + " oi: " + ceotm.getOpenInterest()
								+ " atmCall: " + expiryEntry.getValue().getStrikePrice()
								+ " expiry: " + ceitm.getExpiryDate());
						}
						if( itmltp+otmltp<2*atmltp && ((itmbreakeven + otmbreakeven)/2 >atmbreakeven) && low) {
							float max_loss = ((itmbreakeven + otmbreakeven)/2 - atmbreakeven ) * 2 * lotSize.get(ceitm.getUnderlying());
							//log.info("itmbreakeven: " + itmbreakeven);
							//log.info("otmbreakeven: " + otmbreakeven);
							//log.info("atmbreakeven: " + atmbreakeven);
							//log.info("lot size: " + lotSize.get(ceitm.getUnderlying()));
							if (Math.abs(max_loss)<8000) {
								log.info("possible low side trade: " + entry + " itmCall: " + ceitm.getStrikePrice()
								+ " otmCall: " + ceotm.getStrikePrice() 
								+ " atmCall: " + expiryEntry.getValue().getStrikePrice()
								+ " expiry: " + ceitm.getExpiryDate() + " max_loss: " + max_loss);
							}
						}
						if(itmltp+otmltp>2*atmltp && ((itmbreakeven + otmbreakeven)/2 < atmbreakeven) && high) {
							//log.info("itmbreakeven: " + itmbreakeven + " " + ceitm.getStrikePrice());
							//log.info("otmbreakeven: " + otmbreakeven + " " + ceotm.getStrikePrice());
							//log.info("average: " + (itmbreakeven+otmbreakeven)/2 + " atmbreakeven: " + atmbreakeven);
							log.info("possible highside trade: " + entry + " itmCall: " + ceitm.getStrikePrice()
							+ " otmCall: " + ceotm.getStrikePrice() 
							+ " atmCall: " + expiryEntry.getValue().getStrikePrice()
							+ " expiry: " + ceitm.getExpiryDate());
						}
					}
				}
			}
			
		}
		
		log.info("completed");
	}
	
	private NSEContent getDetails(String stocksymbol) throws Exception{
		client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				  .uri(new URI("https://www.nseindia.com/api/option-chain-equities?symbol="+stocksymbol))
				  .header("accept-encoding", "gzip")
				  .header("cookie", cookie)
				  .GET()
				  .build();
		HttpResponse<InputStream> response = client.send(request, BodyHandlers.ofInputStream());
        int responseStatusCode = response.statusCode();
        //if(response.headers().firstValue(""))
        //log.info("stock: " + stocksymbol);
        //String encoding = response.headers().firstValue("content-encoding").get();
        //log.info("encoding: " + encoding + " stock: " + stocksymbol);
        GZIPInputStream respbodystream = new GZIPInputStream(response.body());
        //System.out.println("httpGetRequest: " + new String(respbodystream.readAllBytes()));
        ObjectMapper mapper = new ObjectMapper();
        byte[] bodyBytes = respbodystream.readAllBytes();
		NSEContent node = mapper.readValue(new String(bodyBytes),NSEContent.class);
		FileOutputStream outputStream = new FileOutputStream("output/" + stocksymbol + ".json");
	    outputStream.write(bodyBytes);

	    outputStream.close();
        return node;
	}
	
	private CE getATMCall(DataContent content) {
		return null;
	}
	
	private List<CE> getOTMCall(List<DataContent> content) {
	
		List<CE> otmCalls = content.stream().filter(item-> {
			return item.getCE()!=null;
		})
		.filter(item-> {
			return item.getCE().getStrikePrice()>item.getCE().getUnderlyingValue() && item.getCE().getOpenInterest()>0;
		})
		.filter(item-> {
			return item.getCE().getLastPrice()>0 && item.getCE().getChange()!=0;
		})
		.map(item-> {
			return item.getCE();
		}).collect(Collectors.toList());
		return otmCalls;
	}
	
	private List<CE> getITMCall(List<DataContent> content) {
		List<CE> itmCalls = content.stream().filter(item-> {
			return item.getCE()!=null;
		})
		.filter(item-> {
			return item.getCE().getStrikePrice()<item.getCE().getUnderlyingValue() && item.getCE().getOpenInterest()>0;
		})
		.filter(item-> {
			return item.getCE().getLastPrice()>0 && item.getCE().getChange()!=0;
		})
		.map(item-> {
			return item.getCE();
		}).collect(Collectors.toList());
		return itmCalls;
	}

}
