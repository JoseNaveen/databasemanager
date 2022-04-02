package com.joey.databasemanager.options;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;

import com.joey.databasemanager.dto.CE;
import com.joey.databasemanager.dto.DataContent;
import com.joey.databasemanager.dto.NSEContent;
import com.joey.databasemanager.dto.OptionChain;

public class OptionsProcessor implements ItemProcessor<NSEContent, OptionChain>{
	
	
	@Value("${trade_mode.both}")
	private boolean both;
	
	@Value("${trade_mode.low_side}")
	private boolean low;
	
	@Value("${trade_mode.high_side}")
	private boolean high;
	
	@Value("#{${nse.stock_lot_size}}")
	private Map<String,Integer> lotSize;
	
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(OptionsProcessor.class);
	@Override
	public OptionChain process(NSEContent item) throws Exception {
		
		log.info("processing item: " + item.getRecords().getUnderlyingValue());
		Map<String,Map<String,CE>> atmTrades = getATMTrades(item);
		Map<String,List<CE>> otmTrades = new HashMap<>();
		Map<String,List<CE>> itmTrades = new HashMap<>();
		String stock_name = null;
		
		List<CE> itmCalls = getITMCall(item.getRecords().getData());
		stock_name = itmCalls.get(0).getUnderlying();
		itmTrades.put(stock_name, itmCalls);
		
		
		
		List<CE> otmCalls = getOTMCall(item.getRecords().getData());
		stock_name = otmCalls.get(0).getUnderlying();
		otmTrades.put(stock_name, otmCalls);
		findProfitableTrades(atmTrades, otmTrades, itmTrades);
		return null;
	}
	
	private void findProfitableTrades(Map<String,Map<String,CE>> atmTrades,
			Map<String,List<CE>> otmTrades,
			Map<String,List<CE>> itmTrades
			) {
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
	
	
	private Map<String,Map<String,CE>> getATMTrades(NSEContent nsecontent) {
		Map<String,Map<String,CE>> atmTrades = new HashMap<>();
		Float cmp = nsecontent.getRecords().getUnderlyingValue();
		Float current_div = null;
		CE ce = null;
		DataContent dcontent;
		dcontent = nsecontent.getRecords().getData().stream().filter(item->{
			return item.getCE()!=null;
		}).findFirst().get();
		String stock_name = dcontent.getCE().getUnderlying();
		log.info("Stock_name: " + stock_name + " cmp: "+ cmp);
		List<String> expiryDates = nsecontent.getRecords().getExpiryDates();
		Map<String,CE> atmExpiryTrades = new HashMap<>();
		atmTrades.put(stock_name,atmExpiryTrades);
		for(String expiryDate: expiryDates) {
			//log.info("Processing expiry: " + expiryDate);
			List<DataContent> tradesAtCurExpiry = nsecontent.getRecords().getData().stream()
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
		return atmTrades;
	}

}
