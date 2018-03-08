//package com.example.reanmae.tryapp;
//
//import android.content.ContentResolver;
//import android.content.ContentUris;
//import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//import android.database.DatabaseUtils;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Environment;
//import android.provider.DocumentsContract;
//import android.provider.MediaStore;
//import android.support.annotation.RequiresApi;
//import android.util.Log;
//import android.webkit.MimeTypeMap;
//
//import java.io.File;
//import java.text.DecimalFormat;
//
///**
// * Created by Rean Mae on 07/03/2018.
// */
//
//public class FileUtils {
//    static final String TAG = "FileUtils";
//    private static final boolean DEBUG = false; // Set to true to enable logging
//
//    public static final String MIME_TYPE_AUDIO = "audio/*";
//    public static final String MIME_TYPE_TEXT = "text/*";
//    public static final String MIME_TYPE_IMAGE = "image/*";
//    public static final String MIME_TYPE_VIDEO = "video/*";
//    public static final String MIME_TYPE_APP = "application/*";
//
//    public static final String HIDDEN_PREFIX = ".";
//
//    public static String getExtension(String uri) {
//        if (uri == null) {
//            return null;
//        }
//
//        int dot = uri.lastIndexOf(".");
//        if (dot >= 0) {
//            return uri.substring(dot);
//        } else {
//            // No extension.
//            return "";
//        }
//    }
//
//    public static boolean isLocal(String url) {
//        if (url != null && !url.startsWith("http://") && !url.startsWith("https://")) {
//            return true;
//        }
//        return false;
//    }
//
//    public static boolean isMediaUri(Uri uri) {
//        return "media".equalsIgnoreCase(uri.getAuthority());
//    }
//
//    public static Uri getUri(File file) {
//        if (file != null) {
//            return Uri.fromFile(file);
//        }
//        return null;
//    }
//
//    public static File getPathWithoutFilename(File file) {
//        if (file != null) {
//            if (file.isDirectory()) {
//                // no file to be split off. Return everything
//                return file;
//            } else {
//                String filename = file.getName();
//                String filepath = file.getAbsolutePath();
//
//                // Construct path without file name.
//                String pathwithoutname = filepath.substring(0,
//                        filepath.length() - filename.length());
//                if (pathwithoutname.endsWith("/")) {
//                    pathwithoutname = pathwithoutname.substring(0, pathwithoutname.length() - 1);
//                }
//                return new File(pathwithoutname);
//            }
//        }
//        return null;
//    }
//
//    public static String getMimeType(File file) {
//
//        String extension = getExtension(file.getName());
//
//        if (extension.length() > 0)
//            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.substring(1));
//
//        return "application/octet-stream";
//    }
//
//    public static String getMimeType(Context context, Uri uri) {
//        File file = new File(getPath(context, uri));
//        return getMimeType(file);
//    }
//
//    public static boolean isLocalStorageDocument(Uri uri) {
//        return LocalStorageProvider.AUTHORITY.equals(uri.getAuthority());
//    }
//
//    public static boolean isExternalStorageDocument(Uri uri) {
//        return "com.android.externalstorage.documents".equals(uri.getAuthority());
//    }
//
//    public static String getDataColumn(Context context, Uri uri, String selection,
//                                       String[] selectionArgs) {
//
//        Cursor cursor = null;
//        final String column = "_data";
//        final String[] projection = {
//                column
//        };
//
//        try {
//            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
//                    null);
//            if (cursor != null && cursor.moveToFirst()) {
//                if (DEBUG)
//                    DatabaseUtils.dumpCursor(cursor);
//
//                final int column_index = cursor.getColumnIndexOrThrow(column);
//                return cursor.getString(column_index);
//            }
//        } finally {
//            if (cursor != null)
//                cursor.close();
//        }
//        return null;
//    }
//
//
//    public static String getPath(final Context context, final Uri uri) {
//
//        if (DEBUG)
//            Log.d(TAG + " File -",
//                    "Authority: " + uri.getAuthority() +
//                            ", Fragment: " + uri.getFragment() +
//                            ", Port: " + uri.getPort() +
//                            ", Query: " + uri.getQuery() +
//                            ", Scheme: " + uri.getScheme() +
//                            ", Host: " + uri.getHost() +
//                            ", Segments: " + uri.getPathSegments().toString()
//            );
//
//        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
//
//        // DocumentProvider
//        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
//            // LocalStorageProvider
//            if (isLocalStorageDocument(uri)) {
//                // The path is the id
//                return DocumentsContract.getDocumentId(uri);
//            }
//            // ExternalStorageProvider
//            else if (isExternalStorageDocument(uri)) {
//                final String docId = DocumentsContract.getDocumentId(uri);
//                final String[] split = docId.split(":");
//                final String type = split[0];
//
//                if ("primary".equalsIgnoreCase(type)) {
//                    return Environment.getExternalStorageDirectory() + "/" + split[1];
//                }
//
//                // TODO handle non-primary volumes
//            }
//            // File
//            else if ("file".equalsIgnoreCase(uri.getScheme())) {
//                return uri.getPath();
//            }
//
//            return null;
//        }
//
//        return null;
//    }
//    public static File getFile(Context context, Uri uri) {
//        if (uri != null) {
//            String path = getPath(context, uri);
//            if (path != null && isLocal(path)) {
//                return new File(path);
//            }
//        }
//        return null;
//    }
//    public static String getReadableFileSize(int size) {
//        final int BYTES_IN_KILOBYTES = 1024;
//        final DecimalFormat dec = new DecimalFormat("###.#");
//        final String KILOBYTES = " KB";
//        final String MEGABYTES = " MB";
//        final String GIGABYTES = " GB";
//        float fileSize = 0;
//        String suffix = KILOBYTES;
//
//        if (size > BYTES_IN_KILOBYTES) {
//            fileSize = size / BYTES_IN_KILOBYTES;
//            if (fileSize > BYTES_IN_KILOBYTES) {
//                fileSize = fileSize / BYTES_IN_KILOBYTES;
//                if (fileSize > BYTES_IN_KILOBYTES) {
//                    fileSize = fileSize / BYTES_IN_KILOBYTES;
//                    suffix = GIGABYTES;
//                } else {
//                    suffix = MEGABYTES;
//                }
//            }
//        }
//        return String.valueOf(dec.format(fileSize) + suffix);
//    }
//    public static Bitmap getThumbnail(Context context, File file) {
//        return getThumbnail(context, getUri(file), getMimeType(file));
//    }
//    public static Bitmap getThumbnail(Context context, Uri uri) {
//        return getThumbnail(context, uri, getMimeType(context, uri));
//    }
//    public static Bitmap getThumbnail(Context context, Uri uri, String mimeType) {
//        if (DEBUG)
//            Log.d(TAG, "Attempting to get thumbnail");
//
//        if (!isMediaUri(uri)) {
//            Log.e(TAG, "You can only retrieve thumbnails for images and videos.");
//            return null;
//        }
//
//        Bitmap bm = null;
//        if (uri != null) {
//            final ContentResolver resolver = context.getContentResolver();
//            Cursor cursor = null;
//            try {
//                cursor = resolver.query(uri, null, null, null, null);
//                if (cursor.moveToFirst()) {
//                    final int id = cursor.getInt(0);
//                    if (DEBUG)
//                        Log.d(TAG, "Got thumb ID: " + id);
//
//                    if (mimeType.contains("video")) {
//                        bm = MediaStore.Video.Thumbnails.getThumbnail(
//                                resolver,
//                                id,
//                                MediaStore.Video.Thumbnails.MINI_KIND,
//                                null);
//                    }
//                    else if (mimeType.contains(FileUtils.MIME_TYPE_IMAGE)) {
//                        bm = MediaStore.Images.Thumbnails.getThumbnail(
//                                resolver,
//                                id,
//                                MediaStore.Images.Thumbnails.MINI_KIND,
//                                null);
//                    }
//                }
//            } catch (Exception e) {
//                if (DEBUG)
//                    Log.e(TAG, "getThumbnail", e);
//            } finally {
//                if (cursor != null)
//                    cursor.close();
//            }
//        }
//        return bm;
//    }
//    public static Intent createGetContentIntent() {
//        // Implicitly allow the user to select a particular kind of data
//        final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        // The MIME data type filter
//        intent.setType("*/*");
//        // Only return URIs that can be opened with ContentResolver
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        return intent;
//    }
//}
//}
