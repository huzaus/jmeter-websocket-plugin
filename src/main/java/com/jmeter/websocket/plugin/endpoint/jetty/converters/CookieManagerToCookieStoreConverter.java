package com.jmeter.websocket.plugin.endpoint.jetty.converters;

import com.google.common.base.Function;
import org.apache.jmeter.protocol.http.control.Cookie;
import org.apache.jmeter.protocol.http.control.CookieManager;
import org.eclipse.jetty.util.HttpCookieStore;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;

import static com.google.common.base.Optional.fromNullable;
import static com.google.common.base.Throwables.propagate;

public class CookieManagerToCookieStoreConverter implements Function<CookieManager, CookieStore> {
    @Override
    public CookieStore apply(CookieManager cookieManager) {
        return fromNullable(cookieManager)
                .transform(new Function<CookieManager, CookieStore>() {
                    @Override
                    public CookieStore apply(CookieManager cookieManager) {
                        HttpCookieStore cookieStore = new HttpCookieStore();
                        for (int i = 0; i < cookieManager.getCookieCount(); i++) {
                            try {
                                Cookie cookie = cookieManager.get(i);
                                cookieStore.add(
                                        new URI(null, cookie.getDomain(), cookie.getPath(), null),
                                        new HttpCookie(cookie.getName(), cookie.getValue())
                                );
                            } catch (URISyntaxException e) {
                                propagate(e);
                            }
                        }
                        return cookieStore;
                    }
                })
                .or(new HttpCookieStore());
    }
}
