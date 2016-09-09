package com.eowise.recyclerview.stickyheaders.samples.NewMessage;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.eowise.recyclerview.stickyheaders.samples.adapters.SendMsgListAdapter;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UploadFileToServer extends AsyncTask<String, String, String> {
    String FILE_UPLOAD_URL = "http://52.76.68.122/lnd/upload.php";
    SendMsgListAdapter chatadapter;
    DonutProgress donut_progress;
    File sourceFile;
    int totalSize = 0;
    String imagepath = "";
    int pos = 0;

    public UploadFileToServer(DonutProgress dp, String imagepath, SendMsgListAdapter adapter, int pos) {
        donut_progress = dp;
        this.imagepath = imagepath;
        chatadapter = adapter;
        this.pos = pos;
    }

    @Override
    protected void onPreExecute() {
        // setting progress bar to zero
        donut_progress.setVisibility(View.VISIBLE);
        donut_progress.setProgress(0);
        sourceFile = new File(imagepath);
        totalSize = (int) sourceFile.length();
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(String... progress) {
        Log.d("PROG", progress[0]);
        donut_progress.setProgress(Integer.parseInt(progress[0])); //Updating progress
    }

    @Override
    protected String doInBackground(String... args) {
        HttpURLConnection.setFollowRedirects(false);
        HttpURLConnection connection = null;

        String fileName = sourceFile.getName();

        try {
            connection = (HttpURLConnection) new URL(FILE_UPLOAD_URL).openConnection();

            connection.setRequestMethod("POST");
            String boundary = "---------------------------boundary";
            String tail = "\r\n--" + boundary + "--\r\n";
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            connection.setDoOutput(true);

            String metadataPart = "--" + boundary + "\r\n"
                    + "Content-Disposition: form-data; name=\"metadata\"\r\n\r\n"
                    + "" + "\r\n";

            String fileHeader1 = "--" + boundary + "\r\n"
                    + "Content-Disposition: form-data; name=\"uploadfile\"; filename=\""
                    + fileName + "\"\r\n"
                    + "Content-Type: application/octet-stream\r\n"
                    + "Content-Transfer-Encoding: binary\r\n";

            long fileLength = sourceFile.length() + tail.length();
            String fileHeader2 = "Content-length: " + fileLength + "\r\n";
            String fileHeader = fileHeader1 + fileHeader2 + "\r\n";
            String stringData = metadataPart + fileHeader;

            long requestLength = stringData.length() + fileLength;
            connection.setRequestProperty("Content-length", "" + requestLength);
            connection.setFixedLengthStreamingMode((int) requestLength);
            connection.connect();

            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes(stringData);
            out.flush();

            int progress = 0;
            int bytesRead = 0;
            byte buf[] = new byte[1024];
            BufferedInputStream bufInput = new BufferedInputStream(new FileInputStream(sourceFile));
            while ((bytesRead = bufInput.read(buf)) != -1) {
                // write output
                out.write(buf, 0, bytesRead);
                out.flush();
                progress += bytesRead; // Here progress is total uploaded bytes

                publishProgress("" + (int) ((progress * 100) / totalSize)); // sending progress percent to publishProgress
            }

            // Write closing boundary and close stream
            out.writeBytes(tail);
            out.flush();
            out.close();

            // Get server response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        } catch (Exception e) {
            Log.e("hi", "Exception: " + Log.getStackTraceString(e));
        } finally {
            if (connection != null) connection.disconnect();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.e("Response", "Response from server: " + result);
        super.onPostExecute(result);
        donut_progress.setVisibility(View.GONE);
        chatadapter.callback(pos, result);
    }

}