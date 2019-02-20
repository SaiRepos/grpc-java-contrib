/*
 *  Copyright (c) 2017, salesforce.com, inc.
 *  All rights reserved.
 *  Licensed under the BSD 3-Clause license.
 *  For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.grpc.contrib.xfcc;

import io.grpc.Context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.salesforce.grpc.contrib.xfcc.XfccQuoteUtil.*;

/**
 * x-forwarded-client-cert (XFCC) is a proxy header which indicates certificate information of part or all of the
 * clients or proxies that a request has flowed through, on its way from the client to the server.
 */
public final class XForwardedClientCert {
    static final String XFCC_BY = "By";
    static final String XFCC_HASH = "Hash";
    static final String XFCC_SAN = "SAN";
    static final String XFCC_URI = "URI";
    static final String XFCC_DNS = "DNS";
    static final String XFCC_SUBJECT = "Subject";

    /**
     * The metadata key used to access any present {@link XForwardedClientCert} objects.
     */
    public static final Context.Key<List<XForwardedClientCert>> XFCC_CONTEXT_KEY = Context.key("x-forwarded-client-cert");

    private String by = "";
    private String hash = "";
    private String sanUri = "";
    private List<String> sanDns = new ArrayList<>();
    private String subject = "";

    void setBy(String by) {
        this.by = by;
    }

    void setHash(String hash) {
        this.hash = hash;
    }

    void setSanUri(String sanUri) {
        this.sanUri = sanUri;
    }

    void setSubject(String subject) {
        this.subject = subject;
    }

    void addSanDns(String sanDns) {
        this.sanDns.add(sanDns);
    }

    /**
     * @return The Subject Alternative Name (SAN) of the current proxy’s certificate.
     */
    public String getBy() {
        return by;
    }

    /**
     * @return The SHA 256 digest of the current client certificate.
     */
    public String getHash() {
        return hash;
    }

    /**
     * @return The URI type Subject Alternative Name field of the current client certificate.
     */
    public String getSanUri() {
        return sanUri;
    }

    /**
     * @return The DNS type Subject Alternative Name field(s) of the current client certificate.
     */
    public Collection<String> getSanDns() {
        return Collections.unmodifiableCollection(sanDns);
    }

    /**
     * @return The Subject field of the current client certificate.
     */
    public String getSubject() {
        return subject;
    }

    @Override
    public String toString() {
        List<String> kvp = new ArrayList<>();
        if (!by.isEmpty()) {
            kvp.add(toKvp(XFCC_BY, enquote(by)));
        }
        if (!hash.isEmpty()) {
            kvp.add(toKvp(XFCC_HASH, enquote(hash)));
        }
        if (!sanUri.isEmpty()) {
            kvp.add(toKvp(XFCC_URI, enquote(sanUri)));
        }
        if (!sanDns.isEmpty()) {
            for (String dns : sanDns) {
                kvp.add(toKvp(XFCC_DNS, enquote(dns)));
            }
        }
        if (!subject.isEmpty()) {
            kvp.add(toKvp(XFCC_SUBJECT, enquote(subject)));
        }

        return String.join(";", kvp);
    }

    private String toKvp(String key, String value) {
        return key + "=" + value;
    }
}
