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
import android.widget.Toast;


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
        if (clip != null) {
            ClipData.Item item = clip.getItemAt(0);
            checkForLink(item.coerceToText(this).toString());
        }
        else{
            showToastAndDie("no text in clipboard");
        }
    }

    private void checkForLinkFromIntent(Intent intent){
        checkForLink(intent.getStringExtra(Intent.EXTRA_TEXT));
    }

    private void checkForLink(String text){
        String tmpText = text.toLowerCase();
        if (tmpText.length() == 0 ||
          !(tmpText.startsWith("http:") || tmpText.startsWith("https")) ) {
            showToastAndDie("text is too short or not a url");
        }
        else{
            sendLink(text);
        }
    }

    private void sendLink(String link){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(browserIntent);
        this.finish();
    }

    private void showToastAndDie(String str){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        this.finish();
    }
}
