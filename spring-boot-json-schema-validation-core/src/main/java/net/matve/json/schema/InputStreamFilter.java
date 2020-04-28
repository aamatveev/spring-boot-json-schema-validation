package net.matve.json.schema;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.StreamUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.util.stream.Collectors;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class InputStreamFilter implements Filter {

    public void destroy() {
        // Nothing to do
    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        ResettableStreamHttpServletRequest wrappedRequest = new ResettableStreamHttpServletRequest(
                (HttpServletRequest) request);
        // wrappedRequest.getInputStream().read();
        String body = wrappedRequest.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        wrappedRequest.resetInputStream();
        chain.doFilter(wrappedRequest, response);

    }

    public void init(FilterConfig arg0) throws ServletException {
        // Nothing to do
    }

    private static class ResettableStreamHttpServletRequest extends
            HttpServletRequestWrapper {

        private byte[] rawData;
        private String body;
        private HttpServletRequest request;
        private ResettableServletInputStream servletStream;

        public ResettableStreamHttpServletRequest(HttpServletRequest request) {
            super(request);
            this.request = request;
            this.servletStream = new ResettableServletInputStream();
        }


        public void resetInputStream() {
            servletStream.stream = new ByteArrayInputStream(rawData);
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            if (rawData == null) {
                rawData = StreamUtils.copyToByteArray(this.request.getInputStream());
                body = new String(rawData);
                servletStream.stream = new ByteArrayInputStream(rawData);
            }
            servletStream.stream.reset();
            return servletStream;
        }

        @Override
        public BufferedReader getReader() throws IOException {
            if (rawData == null) {
                rawData = StreamUtils.copyToByteArray(this.request.getInputStream());
                body = new String(rawData);
                servletStream.stream = new ByteArrayInputStream(rawData);
            }
            servletStream.stream.reset();
            return new BufferedReader(new InputStreamReader(servletStream));
        }


        private class ResettableServletInputStream extends ServletInputStream {

            private InputStream stream;

            @Override
            public int read() throws IOException {
                return stream.read();

            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener listener) {

            }
        }
    }
}
