package sgp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class SGP_TLE_Download {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SGP_TLE_Download d = new SGP_TLE_Download();
		System.out.println(d.downloadTLE("25544"));
	}

	public String downloadTLE(String target) {
		String tle = "";
		try {
			String baseURL = "https://www.space-track.org";
			String authPath = "/auth/login";
			String userName = "ygg001@bucknell.edu";
			String password = "MobileComputing";
			String query = "/basicspacedata/query/class/tle_latest/ORDINAL/1/EPOCH/%3Enow-30/NORAD_CAT_ID/"
					+ target + "/orderby/NORAD_CAT_ID/format/tle";

			CookieManager manager = new CookieManager();
			manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
			CookieHandler.setDefault(manager);

			URL url;

			url = new URL(baseURL + authPath);

			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");

			String input = "identity=" + userName + "&password=" + password;

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			String output;
			// System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				// System.out.println(output);
			}

			url = new URL(baseURL + query);

			br = new BufferedReader(new InputStreamReader((url.openStream())));

			tle = target + "\n";
			while ((output = br.readLine()) != null) {
				// System.out.println(output);
				tle = tle + output + "\n";
			}
			conn.disconnect();

		} catch (MalformedURLException e) {
			// Log.e("SGP_Algorithim",
			// "TLE Download Failed, MalformedURLException");
			e.printStackTrace();
		} catch (IOException e) {
			// Log.e("SGP_Algorithim", "TLE Download Failed, IOException");
			// e.printStackTrace();
		}
		return tle;
	}
}
