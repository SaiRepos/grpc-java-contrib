/*
 *  Copyright (c) 2017, salesforce.com, inc.
 *  All rights reserved.
 *  Licensed under the BSD 3-Clause license.
 *  For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.grpc.contrib.instancemode;

import io.grpc.*;

import java.util.UUID;

public class SessionIdServerInterceptor implements ServerInterceptor {
    public static Context.Key<UUID> SESSION_ID = Context.key("SESSION_ID");

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        UUID sessionId = call.getAttributes().get(SessionIdTransportFilter.SESSION_ID);
        if (sessionId != null) {
            return Contexts.interceptCall(Context.current().withValue(SESSION_ID, sessionId), call, headers, next);
        } else {
            throw new IllegalStateException("SessionIdTransportFilter was not registered with " +
                    "ServerBuilder.addTransportFilter(new SessionIdTransportFilter())");
        }
    }
}
