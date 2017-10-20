package com.myccnice.product;

import com.amazonservices.mws.products.MarketplaceWebServiceProducts;
import com.amazonservices.mws.products.MarketplaceWebServiceProductsClient;
import com.amazonservices.mws.products.MarketplaceWebServiceProductsConfig;
import com.amazonservices.mws.products.model.ListMatchingProductsRequest;
import com.amazonservices.mws.products.model.ListMatchingProductsResponse;
import com.amazonservices.mws.products.model.ListMatchingProductsResult;
import com.amazonservices.mws.products.model.ProductList;

public class DowloadProduct {

    private static String accessKeyId = "AKIAJOP2OOD2KNJZYIMQ";
    private static String secretAccessKey = "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";
    private static String applicationName = "aupokous@hotmail.com";
    private static String sellerId = "AVUGB0RNYCGE8";
    private static String applicationVersion = "2011-10-01";
    private static String connurl = "https://mws.amazonservices.com";
    private static String marketPlaceId = "ATVPDKIKX0DER";

    public static void main(String[] args) {
        MarketplaceWebServiceProductsConfig config = new MarketplaceWebServiceProductsConfig();
        config.setServiceURL(connurl+"/Products/2011-10-01");
        MarketplaceWebServiceProducts client = new MarketplaceWebServiceProductsClient(accessKeyId,secretAccessKey,applicationName,applicationVersion,config);

        ListMatchingProductsRequest request = new ListMatchingProductsRequest();
        request.setSellerId(sellerId);
        request.setMarketplaceId(marketPlaceId);
        request.setQuery("Watches");

        ListMatchingProductsResponse response = client.listMatchingProducts(request);
        ListMatchingProductsResult result = response.getListMatchingProductsResult();
        ProductList list = result.getProducts();
        System.out.println(list.toXML());
    }
}
