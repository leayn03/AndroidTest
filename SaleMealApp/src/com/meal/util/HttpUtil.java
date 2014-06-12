package com.meal.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.meal.bean.Global;

/**
 * @author xiamingxing
 */
public class HttpUtil {

	public static String ACTION_DEFAULT;
	private static String BOUNDARY = null;
	public static String BOUNDARY_PREFIX_DEFAULT;
	public static String CHARSET_DEFAULT;
	public static String CONNECTION_DEFAULT;
	public static String CONTENT_TYPE_DEFAULT;

	public static String DOMAIN_DEFAULT;

	public static String DOWNLOAD_PATH_DEFAULT;

	public static final int ERR_DOWNLOAD_FILEEXIST = 1;

	public static final String FAILURE = "0";
	public static final int FAILURE_DOWNLOAD = -1;
	public static String LINE_END_DEFAULT;
	public static String MODULE_DEFAULT;
	public static String PARAMETERS_DEFAULT;
	public static String PORT_DEFAULT;
	public static String PROJECT_DEFAULT;
	public static final String SUCCESS = "1";
	public static final int SUCCESS_DOWNLOAD = 0;
	public static int TIME_OUT_DEFAULT;
	static {

		DOMAIN_DEFAULT = "http://182.92.11.204";
		PORT_DEFAULT = ":80/";
		PROJECT_DEFAULT = "web/";
		MODULE_DEFAULT = "";
		ACTION_DEFAULT = "index.php";
		PARAMETERS_DEFAULT = "";

	}
	static {

		DOWNLOAD_PATH_DEFAULT = "";

	}
	static {

		CHARSET_DEFAULT = "utf-8";
		CONNECTION_DEFAULT = "Keep-Alive";
		TIME_OUT_DEFAULT = 2 * 10000;
		CONTENT_TYPE_DEFAULT = "multipart/form-data";
		BOUNDARY_PREFIX_DEFAULT = "--";
		LINE_END_DEFAULT = "\r\n";

	}

	/**
	 * @param requestURL
	 * @return
	 */
	private static InputStream _getDownLoadInputStream(String requestURL) {

		InputStream in = null;

		try {

			URL httpURL = new URL(requestURL);

			HttpURLConnection conn = (HttpURLConnection) httpURL
					.openConnection();
			in = conn.getInputStream();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return in;
	}

	/**
	 * @param BOUNDARY
	 * @param file
	 * @return
	 */
	private static String _getFileUploadRequestHeader(File file) {

		StringBuffer buffer = null;

		if (null != file && null != BOUNDARY) {

			buffer = new StringBuffer();
			buffer.append(BOUNDARY_PREFIX_DEFAULT);
			buffer.append(BOUNDARY);
			buffer.append(LINE_END_DEFAULT);
			buffer.append("Content-Disposition: form-data; name=\"file\"; filename=\""
					+ file.getName() + "\"" + LINE_END_DEFAULT);
			buffer.append("Content-Type: application/octet-stream; charset="
					+ CHARSET_DEFAULT + LINE_END_DEFAULT);
			buffer.append(LINE_END_DEFAULT);

		}

		return buffer.toString();

	}

	/**
	 * @param httpURL
	 * @return
	 */
	private static HttpGet _getGetRequest(String httpURL) {

		HttpGet httpRequest = new HttpGet(httpURL);

		return httpRequest;

	}

	/**
	 * @param httpURL
	 * @param postData
	 * @return
	 */
	private static HttpPost _getPostRequest(String httpURL, String postData) {

		HttpPost httpRequest = new HttpPost(httpURL);
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("par", "HttpClient_android_Post"));
		params.add(new BasicNameValuePair("postData", postData));

		HttpEntity httpentity;

		try {

			httpentity = new UrlEncodedFormEntity(params, CHARSET_DEFAULT);
			httpRequest.setEntity(httpentity);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();

		}

		return httpRequest;

	}

	/**
	 * @param conn
	 * @param BOUNDARY
	 */
	private static boolean _initHttpConnection(HttpURLConnection conn) {

		boolean result = false;

		if (null != conn && null != BOUNDARY) {

			try {

				conn.setReadTimeout(TIME_OUT_DEFAULT);
				conn.setConnectTimeout(TIME_OUT_DEFAULT);
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setUseCaches(false);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Charset", CHARSET_DEFAULT);
				conn.setRequestProperty("Connection", CONNECTION_DEFAULT);
				conn.setRequestProperty("Content-Type", CONTENT_TYPE_DEFAULT
						+ ";boundary=" + BOUNDARY);

				result = true;

			} catch (ProtocolException e) {
				// TODO Auto-generated catch block

				e.printStackTrace();

			}

		}

		return result;

	}

	/**
	 * @param action
	 * @param httpResponse
	 * @return
	 */
	private static JSONObject _parserHttpResponse(String action,
			HttpResponse httpResponse) {

		JSONObject jsonDataArea = null;

		if (null != httpResponse) {

			if (null != httpResponse
					&& httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				// 取得返回的字符串
				try {

					writeTokenToLocal(httpResponse);

					String strResponse = EntityUtils.toString(httpResponse
							.getEntity());
					JSONObject jsonResponse = new JSONObject(strResponse);

					if (null != jsonResponse
							&& 0 == jsonResponse.getInt("errno")) {

						jsonDataArea = jsonResponse.getJSONObject("data");

					} else {

						ErrUtil.log(action, "ERR_RESPONSE_DATA");

					}
				} catch (Exception e) {

					e.printStackTrace();

					ErrUtil.log(action, "EXCEPTION_FORMAT_DATA");

				}

			} else {

				ErrUtil.log(action, "FAIL_CONNECT_SERVER");

			}

		}

		return jsonDataArea;

	}

	/**
	 * @param in
	 * @return
	 */
	private static String _parserUploadFileURLFromResponse(InputStream in) {

		String fileURL = null;

		if (null != in) {

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));

			StringBuffer responseBuffer = new StringBuffer();

			try {

				String str = "";

				while ((str = reader.readLine()) != null) {

					responseBuffer.append(str);

				}

				String responseString = responseBuffer.toString();

				if (null != responseString) {

					JSONObject jsonObject = new JSONObject(responseString);

					if (null != jsonObject && 0 == jsonObject.getInt("errno")) {

						JSONObject jsonData = jsonObject.getJSONObject("data");

						if (null != jsonData) {

							fileURL = jsonData.getString("url");

						}

					}

				}

			} catch (Exception e) {
				// TODO Auto-generated catch block

				e.printStackTrace();

			}

		}

		return fileURL;

	}

	/**
	 * @param method
	 * @param module
	 * @param action
	 * @param parameters
	 * @param postData
	 * @return
	 */
	private static JSONObject _REQUEST(String method, String module,
			String action, String parameters, String postData) {

		String httpURL = getURL(module, action, parameters);
		JSONObject jsonDataArea = null;

		try {

			HttpClient httpClient = new DefaultHttpClient();

			httpClient.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT_DEFAULT);
			httpClient.getParams().setParameter(
					CoreConnectionPNames.SO_TIMEOUT, TIME_OUT_DEFAULT);

			HttpResponse httpResponse;

			if (null != method && method.equals("POST")) {

				HttpPost httpRequest_Post = _getPostRequest(httpURL, postData);

				if (null != Global.token) {

					httpRequest_Post.addHeader("Cookie",
							setCookie_token(Global.token));

				}

				httpResponse = httpClient.execute(httpRequest_Post);

			} else {

				HttpGet httpRequest_Get = _getGetRequest(httpURL);

				if (null != Global.token) {

					httpRequest_Get.addHeader("Cookie",
							setCookie_token(Global.token));

				}

				httpResponse = httpClient.execute(httpRequest_Get);

			}

			// writeTokenToLocal(httpClient);

			jsonDataArea = _parserHttpResponse(action, httpResponse);

		} catch (Exception e) {

			e.printStackTrace();

			ErrUtil.log(action, "EXCEPTION_CONNECT_SERVER");

		}

		return jsonDataArea;

	}

	/**
	 * @param conn
	 * @param file
	 * @return
	 */
	private static boolean _uploadFileToServer(HttpURLConnection conn, File file) {

		boolean result = false;

		if (null != conn && null != file) {

			_initHttpConnection(conn);

			try {

				String requestHeader = _getFileUploadRequestHeader(file);

				if (_writeOutputStream(conn, requestHeader, file)) {

					result = true;

				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}

		}

		return result;

	}

	/**
	 * @param conn
	 * @param requestHeader
	 * @param file
	 * @return
	 */
	private static boolean _writeOutputStream(HttpURLConnection conn,
			String requestHeader, File file) {

		boolean result = false;

		if (null != conn && null != requestHeader && null != file) {

			try {

				OutputStream outputSteam = conn.getOutputStream();
				DataOutputStream out = new DataOutputStream(outputSteam);
				out.write(requestHeader.getBytes());

				InputStream fin = new FileInputStream(file);
				byte[] bytes = new byte[1024];
				int len = 0;
				while ((len = fin.read(bytes)) != -1) {

					out.write(bytes, 0, len);

				}
				fin.close();

				out.write(LINE_END_DEFAULT.getBytes());

				byte[] end_data = (BOUNDARY_PREFIX_DEFAULT + BOUNDARY
						+ BOUNDARY_PREFIX_DEFAULT + LINE_END_DEFAULT)
						.getBytes();
				out.write(end_data);
				out.flush();

				result = true;

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}

		}

		return result;

	}

	/**
	 * @param fileName
	 * @param path
	 * @param requestURL
	 * @return
	 */
	public static int DownLoad(String fileName, String path, String requestURL) {

		if (FileUtils.isFileExist(path + fileName)) {

			return ERR_DOWNLOAD_FILEEXIST;

		} else {

			try {

				InputStream input = null;

				input = _getDownLoadInputStream(requestURL);

				File resultFile = FileUtils.write2SDFromInput(path, fileName,
						input);

				if (resultFile == null) {

					return FAILURE_DOWNLOAD;

				}

			} catch (Exception e) {
				// TODO Auto-generated catch block

				e.printStackTrace();

			}

		}

		return SUCCESS_DOWNLOAD;

	}

	/**
	 * @param requestURL
	 * @return
	 */
	public static Bitmap DownLoadImage(String requestURL) {

		Bitmap bitmap = null;

		HttpGet httpRequest = new HttpGet(requestURL);

		try {

			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = httpClient.execute(httpRequest);

			if (null != httpResponse
					&& httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				HttpEntity httpEntity = httpResponse.getEntity();

				InputStream in = httpEntity.getContent();
				bitmap = BitmapFactory.decodeStream(in);
				in.close();

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return bitmap;

	}

	/**
	 * @param requestURL
	 * @return
	 */
	public static String DownLoadTxt(String requestURL) {

		String txtString = null;

		try {

			URL httpURL = new URL(requestURL);
			HttpURLConnection conn = (HttpURLConnection) httpURL
					.openConnection();

			InputStream input = conn.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(input));

			String line = null;
			StringBuffer stringBuffer = new StringBuffer();

			while ((line = in.readLine()) != null) {

				stringBuffer.append(line);

			}

			txtString = stringBuffer.toString();

		} catch (Exception e) {

			e.printStackTrace();

		}

		return txtString;

	}

	/**
	 * @param module
	 * @param action
	 * @param parameters
	 * @return
	 */
	public static JSONObject Get(String module, String action, String parameters) {

		return _REQUEST("GET", module, action, parameters, null);

	}

	/**
	 * @param module
	 * @param action
	 * @param parameters
	 * @return
	 */
	public static String getURL(String module, String action, String parameters) {

		String MODULE = (null != module && 0 != module.length()) ? module + "/"
				: MODULE_DEFAULT;

		String ACTION = (null != action && 0 != action.length()) ? action
				+ ".php" : ACTION_DEFAULT;

		String PARAMETERS = (null != parameters && 0 != parameters.length()) ? ("?" + parameters)
				: PARAMETERS_DEFAULT;

		String httpURL = MessageFormat.format("{0}{1}{2}{3}{4}{5}{6}",
				DOMAIN_DEFAULT, PORT_DEFAULT, MODULE_DEFAULT, PROJECT_DEFAULT,
				MODULE, ACTION, PARAMETERS);

		return httpURL;

	}

	/**
	 * @param module
	 * @param action
	 * @param parameters
	 * @param postData
	 * @return
	 */
	public static JSONObject Post(String module, String action,
			String parameters, String postData) {

		return _REQUEST("POST", module, action, parameters, postData);

	}

	/**
	 * @param requestURL
	 * @param file
	 * @return
	 */
	public static String Upload(String requestURL, File file) {

		String fileURL = null;

		BOUNDARY = UUID.randomUUID().toString();

		if (null != requestURL && null != file) {

			try {

				URL httpURL = new URL(requestURL);
				HttpURLConnection conn = (HttpURLConnection) httpURL
						.openConnection();

				if (_uploadFileToServer(conn, file)) {

					if (HttpStatus.SC_OK == conn.getResponseCode()) {

						fileURL = _parserUploadFileURLFromResponse(conn
								.getInputStream());

						conn.disconnect();

					}

				}

			} catch (Exception e) {

				e.printStackTrace();

			}

		}

		return fileURL;

	}

	/**
	 * @param module
	 * @param parameters
	 * @param file
	 */
	public static String Upload(String module, String parameters, File file) {

		String httpURL = getURL(module, "upload", parameters);

		return Upload(httpURL, file);

	}

	/**
	 * @param httpClient
	 */
	private static String getCookie(HttpClient httpClient) {

		StringBuffer sb = new StringBuffer();

		if (null != httpClient) {

			List<Cookie> cookies = ((AbstractHttpClient) httpClient)
					.getCookieStore().getCookies();

			for (int i = 0; i < cookies.size(); i++) {

				Cookie cookie = cookies.get(i);

				String cookieName = cookie.getName();

				String cookieValue = cookie.getValue();

				if (!TextUtils.isEmpty(cookieName)

				&& !TextUtils.isEmpty(cookieValue)) {

					sb.append(cookieName + "=");

					sb.append(cookieValue + ";");

				}

			}

		}

		return sb.toString();

	}

	/**
	 * @param httpClient
	 * @param cookieName
	 * @return
	 */
	private static String getCookieValue(HttpClient httpClient,
			String cookieName) {

		String result = null;

		if (null != httpClient) {

			List<Cookie> cookies = ((AbstractHttpClient) httpClient)
					.getCookieStore().getCookies();

			for (int i = 0; i < cookies.size(); i++) {

				Cookie cookie = cookies.get(i);

				if (null != cookie.getName()
						&& cookie.getName().equals(cookieName)) {

					result = cookie.getValue();

				}

			}

		}

		return result;

	}

	/**
	 * @param cookies
	 * @param cookieName
	 * @return
	 */
	private static String getCookieValue(String cookies, String cookieName) {

		String result = null;

		if (null != cookies && null != cookieName) {

			String cookiesArray[] = cookies.split(";");

			for (int i = 0; i < cookiesArray.length; i++) {

				String cookieItem = cookiesArray[i];
				String cookieKey = cookieItem.split("=")[0];
				if (null != cookieKey && cookieKey.equals(cookieName)) {

					result = cookieItem.split("=")[1];

				}
			}

		}

		return result;

	}

	/**
	 * @param cookieName
	 * @param cookieValue
	 * @return
	 */
	private static String addCookie(String cookieKey, String cookieValue) {

		StringBuffer sb = new StringBuffer();

		if (null != cookieKey) {

			sb.append(cookieKey + "=");
			sb.append(cookieValue + ";");

		}

		return sb.toString();

	}

	/**
	 * @param token
	 * @return
	 */
	private static String encodeToken(String token) {

		String result = null;

		if (null != token) {

			String timeStamp = SysUtil.getTimeStamp();

			if (null != timeStamp) {

				result = SysUtil.getEncodeBase64(token + timeStamp);

			}
			
			Log.i("test", timeStamp + "  " + token + "  " + result );

		}

		return result;

	}

	/**
	 * @param token
	 * @return
	 */
	private static String setCookie_token(String token) {

		String result = null;

		if (null != token) {

			String encodeToken = encodeToken(token);

			result = addCookie("token", encodeToken);

		}

		return result;

	}

	/**
	 * @param httpResponse
	 */
	private static void writeTokenToLocal(HttpResponse httpResponse) {

		Header cookieHeader = httpResponse.getFirstHeader("Set-Cookie");

		if (cookieHeader != null) {

			String cookies = cookieHeader.getValue();

			String token = getCookieValue(cookies, "token");

			Global.token = token;

		}

	}

	/**
	 * @param httpResponse
	 */
	private static void writeTokenToLocal(HttpClient httpClient) {

		if (httpClient != null) {

			String token = getCookieValue(httpClient, "token");

			Global.token = token;

		}

	}

}
