package com.lepu.serial.task;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @param <T> 任务  任务/ 需要排队的任务， 支持扩展
 */
public class BaseTask<T extends BaseTaskBean> {

    /**
     * 排队容器, 需要排队的任务才会加入此列表
     */
    private LinkedList<T> lineUpBeans;

    /**
     * 执行下一个任务任务的监听器
     */
    private OnTaskListener onTaskListener;

    public BaseTask<T> setOnTaskListener(OnTaskListener<T> onTaskListener) {
        this.onTaskListener = onTaskListener;
        return this;
    }



    public BaseTask() {
        // app 只会执行一直
        lineUpBeans = new LinkedList<>();
    }



    /**
     * 加入排队
     *
     * @param task 一个任务
     */
    public void addTask(T task) {
        if (lineUpBeans != null) {
            Log.e("Post", "任务加入排队中" + task.taskNo);
            if (!checkTask()) {
                lineUpBeans.addLast(task);
                if (onTaskListener != null) {
                    onTaskListener.exNextTask(task);
                }
            } else {
                lineUpBeans.addLast(task);
            }

        }
    }

    /**
     * 检查列表中是否有任务正在执行。
     *
     * @return true 表示有任务正在执行。
     */
    public boolean checkTask() {
        boolean isTask = false;
        if (lineUpBeans != null) {
            if (lineUpBeans.size() > 0) isTask = true;
        }
        return isTask;
    }


    /**
     * 得到下一个执行的计划，
     *
     * @return 返回下一个将要执行的任务， 返回null ,代表没有任务可以执行了
     */
    public BaseTaskBean getFirst() {
        if (lineUpBeans != null) {
            return lineUpBeans.getFirst();
        }
        return null;
    }


    /**
     * 执行成功之后，删除排队中的任务。
     *
     * @param task
     */
    private void deleteTask(T task) {
          if (lineUpBeans != null) {
            for (int i = 0; i < lineUpBeans.size(); i++) {
                BaseTaskBean consumptionTask = lineUpBeans.get(i);
                if (task.taskNo.equals(consumptionTask.taskNo)) {
                    lineUpBeans.remove(consumptionTask); // 删除任务
                    break;
                }
            }
        }
    }


    /**
     * 删除对应父id的所有任务
     *
     * @param planNo
     */
    public void deletePlanNoAll(String planNo) {
        if (lineUpBeans != null) {
            Iterator iterator = lineUpBeans.iterator();
            if (TextUtils.isEmpty(planNo)) return;
            while (iterator.hasNext()) {
                BaseTaskBean task = (BaseTaskBean) iterator.next();
                if (task.planNo.equals(planNo)) {
                    iterator.remove();
                }
            }
        }
    }

    /**
     * 删除对应子id的项
     *
     * @param taskNo
     */
    public void deleteTaskNoAll(String taskNo) {
        if (lineUpBeans != null) {
            Iterator iterator = lineUpBeans.iterator();
            if (TextUtils.isEmpty(taskNo)) return;
            while (iterator.hasNext()) {
                BaseTaskBean task = (BaseTaskBean) iterator.next();
                if (task.taskNo.equals(taskNo)) {
                    iterator.remove();
                    break;
                }
            }
        }
    }


    /**
     * 外部调用， 当执行完成一个任务调用
     */
    public void exOk(T task) {
        AsyncTask.execute(()->{});
        deleteTask(task); // 删除已经执行完成的任务。
        if (lineUpBeans != null) {
            if (checkTask()) {
                // 发现还有任务
                if (onTaskListener != null) {
                    onTaskListener.exNextTask(lineUpBeans.getFirst());
                }
            } else {
                if (onTaskListener != null) {
                    onTaskListener.noTask();
                }
            }
        }
    }





}

