package com.bitdubai.fermat_dmp_plugin.layer.world.coinapult.developer.bitdubai.version_1.coinapult_http_client;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class AddressInfo {
	public static class Json extends GenericJson {
		@Key
		public String address;
		/* Echo back the address passed. */

		@Key
		public String status;
		/*
		 * "unknown" (does not belong to you) or "valid" (belongs to you)
		 */
	}
}
