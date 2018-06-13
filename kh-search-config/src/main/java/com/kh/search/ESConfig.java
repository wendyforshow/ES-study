package com.kh.search;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 所在的包名: com.kh.search
 * 所在的项目名：kh-search
 *
 * @Author:xukh
 * @Description:
 * @Date: Created in 23:56 2018/6/13
 */
@Configuration
public class ESConfig {

    @Bean
    public TransportClient client() throws UnknownHostException {
        InetSocketTransportAddress node = new InetSocketTransportAddress(
                InetAddress.getByName("localhost"),
                9300
        );

        Settings settings = Settings.builder()
                .put("cluster.name", "wali")
                .build();

        PreBuiltTransportClient client = new PreBuiltTransportClient(settings);
        client.addTransportAddress(node);

        return client;
    }
}
