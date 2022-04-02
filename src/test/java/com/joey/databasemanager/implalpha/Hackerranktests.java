package com.joey.databasemanager.implalpha;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import com.joey.databasemanager.DatabaseTests;

public class Hackerranktests {
	
	public static void main(String[] args) throws ParseException {
		List<Integer> arr = new ArrayList<>();
		
		arr.size();
		for (int item: arr) {
			System.out.println(item);
		}
		
		double d = (double)5/(double)6;
		System.out.println(String.format("%.6f", d));
		
		arr.sort(new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				if (o1<o2) {
					return -1;
				}
				if (o1==o2) {
					return 0;
				}
				if (o1>o2) {
					return 1;
				}
				return 0;
			}
			
		});
		int min=0;
		for(int i=0;i<arr.size()-1-1;i++) {
			min = min+ arr.get(i);
		}
		int max=0;
		for(int i=1;i<arr.size()-1;i++) {
			min = min+ arr.get(i);
		}
		System.out.println(min + " " + max);
		
		var time = new SimpleDateFormat("hh:mm:ssaa");
		var parsedtime = time.parse("12:01:00AM");
		var opformat = new SimpleDateFormat("HH:mm:ss");
		System.out.println(opformat.format(parsedtime));
		System.out.println(parsedtime);
		arr.add(100);
		arr.add(101);
		int high = arr.get(0);
		int low = arr.get(0);
		int no_high_broken = 0;
		int no_low_broken = 0;
		for (int i=1;i<arr.size();i++) {
			if (arr.get(i)>high) {
				no_high_broken++;
				high = arr.get(i);
			}
			if (arr.get(i)<low) {
				no_low_broken++;
				low = arr.get(i);
			}
			
		}
		System.out.println(no_high_broken + " " + no_low_broken);
		
		var packages = Package.getPackages();
		for (Package p: packages) {
			System.out.println(p.getName());
		}
		String json = """
				{
					"name": "josenaveen"
				}
				""";
		
		
		
		
	}

}
