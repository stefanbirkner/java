	   //1. Get the bytes
	   byte[] orderBytes;
            try {
                orderBytes = orderData.getBytes(Constants.UTF8);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Encoding UTF8 does not exist (is required to exist in java)");
            }
            ByteArrayOutputStream baout = new ByteArrayOutputStream();

            //2. GZIP bytes
            GZIPOutputStream zip;
            try {
                zip = new GZIPOutputStream(baout);
                zip.write(orderBytes);
                zip.finish();
                //3. Encode Base64         
                orderData = new String(Base64.encodeBase64(baout.toByteArray()), Constants.UTF8);
                zip.close();
            } catch (IOException e) {
                throw new RuntimeException("Unable to create zipped and Base64 encoded orderData due to:"+e.getMessage());
	    }
