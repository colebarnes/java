/*
 * Copyright © 2026 cole@colebarnes.org, https://colebarnes.org
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the “Software”), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR  COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.colebarnes.common;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class HttpUtils {
  public static String urlEncode(Map<String, String> data) {
    // TODO: check input ...
    StringBuilder sb = new StringBuilder();

    for (Entry<String, String> entry : data.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();

      if (!StringUtils.isNullOrBlank(key)) {
        if (sb.length() > 0) {
          sb.append("&");
        }

        sb.append(URLEncoder.encode(key, StringUtils.defaultCharset()));

        if (!StringUtils.isNullOrBlank(value)) {
          sb.append("=");
          sb.append(URLEncoder.encode(value, StringUtils.defaultCharset()));
        }
      }
    }

    return sb.toString();
  }

  public static Map<String, String> urlDecode(String urlEncoded) {
    // TODO: check input ...
    Map<String, String> data = new HashMap<>();
    String[] pairs = urlEncoded.split(Pattern.quote("&"), 0);

    if (pairs != null && pairs.length > 0) {
      for (String pair : pairs) {
        if (!StringUtils.isNullOrBlank(pair)) {
          String[] keyValue = pair.split(Pattern.quote("="), 2);

          if (keyValue.length > 0) {
            String key = URLDecoder.decode(keyValue[0], StringUtils.defaultCharset());
            String value = keyValue.length > 1 ? URLDecoder.decode(keyValue[1], StringUtils.defaultCharset()) : "";
            data.put(key, value);
          }
        }
      }
    }

    return data;
  }
}
