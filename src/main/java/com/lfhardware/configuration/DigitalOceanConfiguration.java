package com.lfhardware.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.async.SdkAsyncHttpClient;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3AsyncClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;

@Configuration
public class DigitalOceanConfiguration {

    @Value("${digitalocean.endpoint}")
    private String endpoint;

    @Value("${digitalocean.region}")
    private String region;

    @Value("${digitalocean.credentials.access-key-id}")
    private String accessKeyId;

    @Value("${digitalocean.credentials.secret-access-key}")
    private String secretAccessKey;

    @Bean
    public S3AsyncClient amazonS3() throws URISyntaxException {

        AwsCredentialsProvider awsCredentialsProvider = StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKeyId, secretAccessKey));
            SdkAsyncHttpClient httpClient = NettyNioAsyncHttpClient.builder()
                    .writeTimeout(Duration.ZERO)
                    .maxConcurrency(64)
                    .build();
        S3Configuration serviceConfiguration = S3Configuration.builder()
//                .checksumValidationEnabled(false)
                .chunkedEncodingEnabled(true)
                .build();
            S3AsyncClientBuilder asyncClientBuilder = S3AsyncClient.builder().httpClient(httpClient)
                    .region(Region.of(this.region))
                    .credentialsProvider(awsCredentialsProvider)
//                    .forcePathStyle(true)
//                    .multipartEnabled(true)
                    .endpointOverride(new URI(endpoint));
//                    .serviceConfiguration(serviceConfiguration);
//                    .targetThroughputInGbps(20.0) //
//                    .minimumPartSizeInBytes(10 * SizeConstant.MB) //;

            return asyncClientBuilder.build();
    }
}
