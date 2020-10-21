package com.amazonaws.msk.auth.iam.internals;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.RegionMetadata;
import com.amazonaws.regions.RegionMetadataFactory;

import java.time.Instant;
import java.util.Objects;

/**
 * This class represents the parameters that will be used to generate the Sigv4 signature
 * as well as the final AuthenticationRequestPayload (except the signature itself) sent to the kafka broker.
 * These are the parameters that will go into this class.
 **/

public class AuthenticationRequestParams {
    private static final String VERSION_1 = "V1";
    private static final String SIGNED_HEADERS = "host";
    private static final String SERVICE_SCOPE = "kafka-cluster";

    private static RegionMetadata regionMetadata = RegionMetadataFactory.create();

    private final String host;
    private final AWSCredentials awsCredentials;
    private final Region region;

    AuthenticationRequestParams(String host,
            AWSCredentials awsCredentials,
            Region region) {
        this.host = Objects.requireNonNull(host);
        this.awsCredentials = Objects.requireNonNull(awsCredentials);
        this.region = Objects.requireNonNull(region);
    }

    public String getServiceScope() {
        return SERVICE_SCOPE;
    }

    public Region getRegion() {
        return region;
    }

    public String getHost() {
        return host;
    }

    public AWSCredentials getAwsCredentials() {
        return awsCredentials;
    }

    public static AuthenticationRequestParams create(String host, AWSCredentials credentials)
            throws IllegalArgumentException {
        Objects.nonNull(host);
        Region region = regionMetadata.tryGetRegionByEndpointDnsSuffix(host);
        if (region == null) {
            throw new IllegalArgumentException("Host " + host + " does not belong to a valid region.");
        }
        return new AuthenticationRequestParams(host, credentials, region);
    }
}
