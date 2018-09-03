package androidworkingspacemartin.androidworkingspace.aopLog.manager;

import java.util.LinkedList;

import androidworkingspacemartin.androidworkingspace.aopLog.model.MsgModel;

/**
 * Created by martinwang on 2017/9/14.
 */

public interface ILogStore {
    /**
     * 存储log
     * @param list      log的集合
     * @param count     存储多少条数据
     * @return          true 成功 false 失败
     */
    boolean storeLog(LinkedList<MsgModel> list, int count);
}
