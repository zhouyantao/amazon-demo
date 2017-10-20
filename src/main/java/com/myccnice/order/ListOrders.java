package com.myccnice.order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrders;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrdersByNextTokenRequest;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrdersByNextTokenResponse;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrdersByNextTokenResult;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrdersRequest;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrdersResponse;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrdersResult;
import com.amazonservices.mws.orders._2013_09_01.model.Order;
import com.myccnice.utils.DateUtils;

public class ListOrders {

    /**
     * 查询时间起点
     */
    private final Date lastUpdatedAfter;

    /**
     * 店铺配置信息
     */
    private final MarketplaceWebServiceOrdersSampleConfig config;

    /**
     * 需要下载的订单状态列表
     */
    private final List<String> DOWNLOAD_ORDER_STATUS = Arrays.asList("Unshipped", "PartiallyShipped");

    /**
     * 每页查询条数
     */
    private static final int PAGE_SIZE = 100;

    public ListOrders(MarketplaceWebServiceOrdersSampleConfig config, Date lastUpdatedAfter) {
        this.config = config;
        this.lastUpdatedAfter = lastUpdatedAfter;
    }

    public List<Order> listOrders() {
        MarketplaceWebServiceOrders client = config.getAsyncClient();
        ListOrdersRequest request = new ListOrdersRequest();
        request.setSellerId(config.getSellerId());
        try {
            request.setLastUpdatedAfter(DatatypeFactory.newInstance().newXMLGregorianCalendar(DateUtils.formatUTCDate(lastUpdatedAfter)));
            request.setOrderStatus(DOWNLOAD_ORDER_STATUS);
            request.setMarketplaceId(Arrays.asList(config.getMarketplaceId()));
            request.setMaxResultsPerPage(PAGE_SIZE);
            ListOrdersResponse response = client.listOrders(request);
            ListOrdersResult orderList = response.getListOrdersResult();
            List<Order> oList = orderList.getOrders();
            String nextToken = orderList.getNextToken(); // 获取下一页的令牌
            if (nextToken != null) {
                oList.addAll(listOrdersByNextToken(client, nextToken));
            }
            return oList;
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * 该 操作返回下一页订单，通过使用 NextToken 的值来完成此操作，该值是通过您上一步请求 ListOrders 或 ListOrdersByNextToken来进行返回。
     * 如果未返回 NextToken 的值，则不会返回其他页面。该 ListOrders 和 ListOrdersByNextToken 操作的最大请求限额为 6 个，恢复速率为每分钟 1 个请求。
     * 有关限制术语的相关定义，请参阅
     * @see <a href="http://docs.developer.amazonservices.com/zh_CN/orders/2013-09-01/Orders_Overview.html">订单 API</a>
     *
     * @param nextToken 您之前对 ListOrders 或 ListOrdersByNextToken的请求的响应中返回的字符串标记。
     * @return 订单列表
     */
    private List<Order> listOrdersByNextToken(MarketplaceWebServiceOrders client, String nextToken) {
        ListOrdersByNextTokenRequest nextTokenRequest = null;
        List<Order> list = new ArrayList<>();
        while (nextToken != null) {
            nextTokenRequest = new ListOrdersByNextTokenRequest(config.getSellerId(), nextToken);
            ListOrdersByNextTokenResponse response = client.listOrdersByNextToken(nextTokenRequest);
            ListOrdersByNextTokenResult result = response.getListOrdersByNextTokenResult();
            list.addAll(result.getOrders());
            nextToken = result.getNextToken();
        }
        return list;
    }

}
