# Link Router
In Android, you cannot send a link to another browser. Link router solves that.

## Usage
Share a link to Router app and it will show options for opening in another browser. Alternatively, if you have a link copied to the pasteboard, clicking on the Router app icon will grab that copied link and offer to open the link in a browser. 

## Other Notes 
Router always calls `this.finish()` when it's done processing a link, so the application will never stick around. Also the attribute `android:excludeFromRecents="true"` is in the app manifest preventing it from showing up on the recent-apps list.