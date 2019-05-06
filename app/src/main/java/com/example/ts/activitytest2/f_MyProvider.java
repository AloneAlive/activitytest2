package com.example.ts.activitytest2;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.CancellationSignal;

/**
 * 内容提供器，继承ContentProvider,并且需要重写6个方法
 */
public class f_MyProvider extends ContentProvider {

    public static final int TABLE1_DIR = 0;

    public static final int TABLE1_ITEM = 1;

    public static final int TABLE2_DIR = 2;

    public static final int TABLE2_ITEM = 3;

    /**
     * 实现匹配内容URI的功能
     * TODO 提供一个addUri()方法,传入三个参数：authority, path, 自定义代码
     * 提供match()方法
     */
    private static UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.example.app.provider", "table1", TABLE1_DIR);   //TODO 添加数据
        uriMatcher.addURI("com.example.app.provider", "table1/#", TABLE1_ITEM);
        uriMatcher.addURI("com.example.app.provider", "table2", TABLE2_DIR);
        uriMatcher.addURI("com.example.app.provider", "table2/#", TABLE2_ITEM);
    }

    /**
     * 初始化内容提供器
     * @return false表示失败
     */
    @Override
    public boolean onCreate() {
        return false;
    }

    /**
     * 查询数据
     * TODO 内容URI写法： content://com.example.app.provider/table1 表示访问com.example.app这个APP的table1表的数据
     * TODO 可以在URI后添加数据： content://com.example.app.provider/table1/1 表示该表id=1的数据
     * TODO 匹配任意表的内容URI： content://com.example.app.provider/*
     * TODO 匹配table1表任意一行的数据的URI： content://com.example.app.provider/table1/# （#表示任意长度的数字）
     * @param uri 确定查询哪张表
     * @param projection 确定查询哪些列
     * @param selection 约束查询哪些行
     * @param selectionArgs 约束查询哪些行
     * @param sortOrder 排序
     * @param cancellationSignal
     * @return 查询结果
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder, CancellationSignal cancellationSignal) {

        switch (uriMatcher.match(uri)) {
            case TABLE1_DIR:
                //查询table1表的所有数据
                break;
            case TABLE1_ITEM:
                //查询table1表的单条数据
                break;
            case TABLE2_DIR:
                //查询table2表的所有数据
                break;
            case TABLE2_ITEM:
                //查询table2表的单条数据
                break;
            default:
                break;
        }
        return null;
    }

    /**
     * 查询数据
     * @param uri
     * @param strings
     * @param s
     * @param strings1
     * @param s1
     * @return
     */
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        return null;
    }

    /**
     * 添加一条数据
     * @param uri 确定要添加的表
     * @param contentValues 待添加的数据
     * @return
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    /**
     * 更新内容提供器已有的数据
     * @param uri 确定更新哪张表
     * @param contentValues 内容参数
     * @param s
     * @param strings
     * @return
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    /**
     * 删除数据
     * @param uri
     * @param s
     * @param strings
     * @return
     */
    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    /**
     * 根据传入的内容URI来返回响应的MIME类型
     * TODO 一个内容URI所对应的MIME字符串由三部分组成：
     *  TODO 必须以vnd开头；
     *  TODO 如果URI以路径结尾,则后接android.cursor.dir/, 如果URI以id结尾，则后接android.cursor.item/
     *  TODO 最后接上vnd.<authority>.<path>
     *      TODO 例如： vnd.android.cursor.dir/vnd.com.example.app.provider.table1
     *      TODO       vnd.android.cursor.item/vnd.com.example.app.provider.table1   (没有id)
     * @param uri
     * @return
     */
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case TABLE1_DIR:
                //table1表的所有数据
                return "vnd.android.cursor.dir/vnd.com.example.app.provider.table1";
            case TABLE1_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.app.provider.table1";
            case TABLE2_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.app.provider.table2";
            case TABLE2_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.app.provider.table2";
            default:
                break;
        }
        return null;
    }
}
