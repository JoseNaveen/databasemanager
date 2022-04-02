package com.joey.databasemanager.options;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

import org.slf4j.LoggerFactory;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joey.databasemanager.dto.NSEContent;

public class OptionsReader extends AbstractItemCountingItemStreamItemReader<NSEContent>{

	List<NSEContent> list;
	
	@Value("${nse.cookie}")
	private String cookie;
	
	
	@Value("#{${nse.stock_lot_size}}")
	private Map<String,Integer> lotSize;
	
	private int current_index = 0;
	
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(OptionsReader.class);

	private HttpClient client;
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
        log.info("stock: " + stocksymbol);
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
	
	@Override
	protected NSEContent doRead() throws Exception {
		if(current_index < list.size()-1) {
			NSEContent item = list.get(current_index);
			current_index = current_index + 1;
			return item;
		}
		return null;
	}
	
	@Override
	protected void doOpen() throws Exception {
		list = lotSize.keySet().stream().map(item-> {
			try {
				return getDetails(item);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}).collect(Collectors.toList());
		current_index = 0;
		setName("options_reader");
	}

	@Override
	protected void doClose() throws Exception {
		list.clear();
		current_index = 0;
	}
	
	

}
