package com.myccnice.order;

import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrders;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersClient;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersConfig;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrderItemsByNextTokenRequest;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrderItemsByNextTokenResponse;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrderItemsByNextTokenResult;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrderItemsRequest;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrderItemsResponse;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrderItemsResult;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrdersByNextTokenRequest;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrdersByNextTokenResponse;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrdersByNextTokenResult;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrdersRequest;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrdersResponse;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrdersResult;
import com.amazonservices.mws.orders._2013_09_01.model.Order;
import com.amazonservices.mws.orders._2013_09_01.model.OrderItem;

public class DowloadOrder {

    private static String accessKeyId = "AKIAJOP2OOD2KNJZYIMQ";
    private static String secretAccessKey = "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";
    private static String applicationName = "aupokous@hotmail.com";
    private static String sellerId = "AVUGB0RNYCGE8";
    private static String applicationVersion = "2013-09-01";
    private static String connurl = "https://mws.amazonservices.com";
    private static String marketPlaceId = "ATVPDKIKX0DER";

    public static void main(String[] args) {
        MarketplaceWebServiceOrdersConfig config = new MarketplaceWebServiceOrdersConfig();
        config.setServiceURL(connurl + "/Orders/2013-09-01");
        MarketplaceWebServiceOrders client = new MarketplaceWebServiceOrdersClient(accessKeyId,secretAccessKey,applicationName,applicationVersion,config);
        ListOrdersRequest request = new ListOrdersRequest();
        request.setSellerId(sellerId);
        try {
            request.setLastUpdatedAfter(DatatypeFactory.newInstance().newXMLGregorianCalendar("2017-10-08T10:10:10.000Z"));
            request.setLastUpdatedBefore(DatatypeFactory.newInstance().newXMLGregorianCalendar("2017-10-10T10:10:10.000Z"));
            List<String> orderStatus = new ArrayList<String>();
            orderStatus.add("Unshipped");
            orderStatus.add("PartiallyShipped");
            orderStatus.add("Shipped");
            request.setOrderStatus(orderStatus);
            List<String> marketplaceId = new ArrayList<String>();
            marketplaceId.add(marketPlaceId);
            request.setMarketplaceId(marketplaceId);
            request.setMaxResultsPerPage(2);
            ListOrdersResponse response = client.listOrders(request);
            ListOrdersResult orderList = response.getListOrdersResult();
            List<Order> oList = orderList.getOrders();
            String nextToken = orderList.getNextToken(); // 获取下一页的令牌
            if (nextToken != null) {
                oList.addAll(listOrdersByNextToken(client, nextToken));
            }
            for(Order order : oList) {
                listOrderItems(client, order.getAmazonOrderId());
            }
        } catch (DatatypeConfigurationException e) {
            System.out.println("DatatypeConfigurationException:"+e.getMessage());
        }
    }

    private static List<Order> listOrdersByNextToken(MarketplaceWebServiceOrders client, String nextToken) {
        ListOrdersByNextTokenRequest nextTokenRequest = null;
        List<Order> list = new ArrayList<>();
        while (nextToken != null) {
            nextTokenRequest = new ListOrdersByNextTokenRequest(nextToken, nextToken);
            nextTokenRequest.setNextToken(nextToken);
            nextTokenRequest.setSellerId(sellerId);
            ListOrdersByNextTokenResponse response = client.listOrdersByNextToken(nextTokenRequest);
            ListOrdersByNextTokenResult result = response.getListOrdersByNextTokenResult();
            list.addAll(result.getOrders());
            nextToken = result.getNextToken();
        }
        return list;
    }

    /**
     * 该 ListOrderItems 操作可为您所指定的 AmazonOrderId 返回订单商品信息。
     * 订单商品信息包括 Title、ASIN、 SellerSKU、ItemPrice、 ShippingPrice 以及税费和促销信息。
     * 您可以通过 ListOrders 操作来检索订单商品信息，进而找到您在指定时间段内所创建或更新的订单。
     * 所返回的订单中包含 AmazonOrderId。然后，您便可以通过操作使用这些 AmazonOrderId 值，ListOrderItems 以获取每个订单的详细订单商品信息。
     * 请注意：
     *  如果订单处于“等待”状态（即已下订单，但未进行付款），则该 ListOrderItems 操作不会返回订单商品的价格、税费、运费、礼品包装或促销信息。
     *  当订单状态从“等待”变为“未配送”、“部分配送”或“已配送”状态（即已完成付款）后，ListOrderItems 操作将返回订单商品的价格、税费、运费、礼品包装和促销信息。
     * @param amazonOrderId
     */
    private static List<OrderItem> listOrderItems(MarketplaceWebServiceOrders client, String amazonOrderId) {
        ListOrderItemsRequest request = new ListOrderItemsRequest(sellerId, amazonOrderId);

        ListOrderItemsResponse response = client.listOrderItems(request);
        ListOrderItemsResult result = response.getListOrderItemsResult();
        System.out.println(result.toXML());
        List<OrderItem> orderItems = result.getOrderItems();
        String nextToken = result.getNextToken();
        if (nextToken != null) {
            orderItems.addAll(listOrderItemsByNextToken(client, nextToken));
        }
        return orderItems;
    }

    private static List<OrderItem> listOrderItemsByNextToken(MarketplaceWebServiceOrders client, String nextToken) {
        List<OrderItem> orderItems = new ArrayList<>();
        ListOrderItemsByNextTokenRequest request = null;
        while (nextToken != null) {
            request = new ListOrderItemsByNextTokenRequest(sellerId, nextToken);
            ListOrderItemsByNextTokenResponse response = client.listOrderItemsByNextToken(request);
            ListOrderItemsByNextTokenResult result = response.getListOrderItemsByNextTokenResult();
            orderItems.addAll(result.getOrderItems());
            nextToken = result.getNextToken();
        }
        return orderItems;
    }
}
