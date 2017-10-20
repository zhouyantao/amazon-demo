package com.myccnice.order;

import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersAsyncClient;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersClient;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersConfig;

/**
 * 订单配置信息
 *
 * create in 2017年10月19日
 * @author wangpeng
 */
public class MarketplaceWebServiceOrdersSampleConfig {

    private static final String ORDERS = "/Orders/";

    /** The client application version. */
    private static final String APP_VERSION = "2013-09-01";

    /** Developer AWS access key. */
    private final String accessKey;

    /** Developer AWS secret key. */
    private final String secretKey;

    /** The client application name. */
    private final String appName;

    /**
     * The endpoint for region service and version.
     * ex: serviceURL = MWSEndpoint.NA_PROD.toString();
     */
    private final String serviceURL;

    private final String sellerId;

    private final String marketplaceId;

    /** The client, lazy initialized. Async client is also a sync client. */
    private MarketplaceWebServiceOrdersAsyncClient client = null;

    
    public MarketplaceWebServiceOrdersSampleConfig(String accessKey,
            String secretKey, String appName, String serviceURL,
            String sellerId, String marketplaceId) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.appName = appName;
        this.serviceURL = serviceURL;
        this.sellerId = sellerId;
        this.marketplaceId = marketplaceId;
    }

    /**
     * Get a client connection ready to use.
     *
     * @return A ready to use client connection.
     */
    public MarketplaceWebServiceOrdersClient getClient() {
        return getAsyncClient();
    }

    /**
     * Get an async client connection ready to use.
     *
     * @return A ready to use client connection.
     */
    public MarketplaceWebServiceOrdersAsyncClient getAsyncClient() {
        if (client==null) {
            MarketplaceWebServiceOrdersConfig config = new MarketplaceWebServiceOrdersConfig();
            config.setServiceURL(serviceURL + ORDERS + APP_VERSION);
            // Set other client connection configurations here.
            client = new MarketplaceWebServiceOrdersAsyncClient(accessKey, secretKey, appName, APP_VERSION, config);
        }
        return client;
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getMarketplaceId() {
        return marketplaceId;
    }
}
