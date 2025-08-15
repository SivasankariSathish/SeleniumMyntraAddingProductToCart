package Utilities;

import java.io.IOException;

import org.testng.annotations.DataProvider;

public class DataProviders {
	
	@DataProvider(name="productList")
	public String[][] productDetils() throws IOException
	{
		String path=".\\testData\\TestData.xlsx";
		ExcelUtility xutil=new ExcelUtility(path);
		int totalrows= xutil.getRowCount("addProduct");
		int totalcols=xutil.getCellCount("addProduct", totalrows);
		String productData[][]=new String[totalrows][totalcols];
		for(int i=1;i<=totalrows;i++)
		{
			for(int j=0;j<totalcols;j++)
			{
				productData[i-1][j]=xutil.getCellData("addProduct", i, j);
			}
		}
		return productData;
		
	}

}
