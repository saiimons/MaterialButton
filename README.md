MaterialButton
==============

Material design button in Jelly Bean and Kita.

## Screenshot ##

![Imgur](http://i.imgur.com/XBuruy8.png)

## Usage ##

```xml
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:mb="http://schemas.android.com/apk/res-auto"
    android:background="#fff0f0f0"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <com.keith.mb.MaterialButton
        mb:text="Material"
        mb:text_color="#ff424242"
        mb:text_size="16sp"
        mb:color_normal="#ffffffff"
        mb:color_ripple="#ffd4d4d4"
        mb:color_shadow="#ffd4d4d4"
        mb:corner_radius="2dp"
        android:layout_centerInParent="true"
        android:layout_width="150dp"
        android:layout_height="50dp"/>
</RelativeLayout>
```

## License ##

Licensed under the MIT license
