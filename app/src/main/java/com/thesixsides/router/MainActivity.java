package com.thesixsides.router;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipData.Item;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {

    Boolean intentSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intentSend = false;

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                intentSend = true;
                checkForLinkFromIntent(intent); // Handle text being sent
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!intentSend){
            checkClipBoardForLink();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkClipBoardForLink(){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = clipboard.getPrimaryClip();
        String clipboardText = "";
        if (clip != null) {
            ClipData.Item item = clip.getItemAt(0);
            checkForLink(item.coerceToText(this).toString());
        }
        else{
            return;
        }
    }

    private void checkForLinkFromIntent(Intent intent){
        checkForLink(intent.getStringExtra(Intent.EXTRA_TEXT));
    }

    private void checkForLink(String text){
        text = text.toLowerCase();
        if (text.length() == 0) {
            return;
        }
        else if ( !(text.substring(0, 5).equals("http:") || text.substring(0, 5).equals("https")) ){
            return;
        }
        else{
            sendLink(text);
        }
    }

    private void sendLink(String link){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(browserIntent);
    }
}
