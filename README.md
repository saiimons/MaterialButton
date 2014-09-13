MaterialButton
==============

Material design button in Jelly Bean and Kita.

## Screenshot ##

![Imgur](http://i.imgur.com/HFHa8M8.png)

## Usage ##

### Text Button ###
```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:mb="http://schemas.android.com/apk/res-auto"
    android:background="#fff0f0f0"
    android:gravity="center"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <com.keith.mb.MaterialButton
        mb:text="Material"
        mb:text_color="#ff424242"
        mb:text_size="16sp"
        mb:color_normal="#ffffffff"
        mb:color_ripple="#ffeaeaea"
        mb:color_shadow="#ffd4d4d4"
        mb:corner_radius="2dp"
        android:layout_width="150dp"
        android:layout_height="50dp"/>
</LinearLayout>
```

### Floating Button ###
```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:mb="http://schemas.android.com/apk/res-auto"
    android:background="#fff0f0f0"
    android:gravity="center"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <com.keith.mb.MaterialButton
        mb:icon="@drawable/add"
        mb:color_normal="#ff303f9f"
        mb:color_ripple="#ff283593"
        mb:color_shadow="#ffbebebe"
        mb:corner_radius="32dp"
        android:layout_width="64dp"
        android:layout_height="64dp"/>
</LinearLayout>
```

## License ##

Licensed under the MIT license
