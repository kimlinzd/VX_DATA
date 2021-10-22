package com.lepu.algorithm.ecg.utils;

import android.view.KeyEvent;

/**
 * Created by wuxd
 * @author wxd
 */
public class Const {

    //12导联设备 SUPPORT_ALL_LEAD true or false;
    //18导联设备 SUPPORT_ALL_LEAD 必须为true
//    public static AppTypeEnum appTypeEnum = AppTypeEnum.values()[BuildConfig.APP_TYPE_ENUM];
    //是否支持所有导联显示 12导联设备：true 可以模拟显示18导联数据;false 显示12导联数据
//    public static boolean SUPPORT_ALL_LEAD = BuildConfig.SUPPORT_ALL_LEAD;
    /**
     * 0. 院内版，默认本地版，可激活AI云；glassgo 需要激活
     * 1. 运营版，默认AI云板，不可切换本地；
     * 2. 英文院内版：默认英文，可激活AI云，标配Glasgow，可切换本地算法
     */
//    public static VersionConfigEnum.AppVersion appVersion = VersionConfigEnum.AppVersion.values()[BuildConfig.APP_VERSION];

    //是否支持热敏打印，暂时12，18导联都支持，传true
    public static boolean SUPPORT_THERMAL_PRINT = true;
    //热敏打印设备是smartecg
    public static final boolean THERMAL_PRINT_DEVICE_SMARTECG = true;
    //是否显示底部导航栏
//    public static  final boolean IS_SHOW_BOTTOM_NAV_BAR = BuildConfig.DEBUG;
    //是否是大屏幕.
    //SmartEcg 10: 1280 800 true; 7: false. 1024 600
    //C120 false
    public static boolean BIG_SCRREN = true;
    //热敏打印，同步发送，同步发送
    public static final boolean THERMAL_PRINT_DEVICE_SMARTECG_SYNC = false;

    public static final String TAG_MONITOR = "MonitorInfo";
    public static final int PRINT_LINE_COUNT = 6;//6 插值算法点数
    public static  String MAIN_AUTH_PWD = "135790";//本地登录密码
    public static  String SYSTEM_AUTH_PWD = "24680";//厂家维护密码
    public static  final int MAX_LOGIN_ERROR_TIMES=5;//登录失败最大次数
    public static  final int MAX_LOGIN_ERROR_TIMESTAMP=10*60*1000;//登录失败超过最大次数等待的时长
    public static final int SAMPLE_RATE = 1000;//采样率
    public static final float SMART_ECG_TRADITION_ANALYSIS_TEMPLATE = 2.52F;//平均模板增益 2.52F
    public static final int LEAD_ADD_EXTRA_COUNT = 2;//增加除了心电数据的导联
    public static final byte THERMAL_PRINT_COLUME = THERMAL_PRINT_DEVICE_SMARTECG ? 20 : 10;//c120 打印列数8;smart 打印列数

    //scp C120:5000/2048*1.035=2520 ;SmartEcg:
    //换算不一样，C120的是6553.6对应1mV  SmartECG的是1048.576对应1mV
    //smartEcg
    public static final float MV_1_SHORT = 1048.576F;
    public static final float SHORT_MV_GAIN = 0.0009536F;//mv
    public static final float GAIN = 0.9536F;//uv
    public static final float SHORT_EXTRA_VALUE = 1.035F;//采集的原始数据幅值少，补充 1035/1000f.SmartEcg 不用补充

    //C120
//    public static final float MV_1_SHORT = 6553.6F;
//    public static final float SHORT_MV_GAIN = 5/2048f/16;
//    public static final float GAIN = SHORT_MV_GAIN*1000;//uv
//    public static final float SHORT_EXTRA_VALUE = 1.035F;//采集的原始数据幅值少，补充 1035/1000f.SmartEcg 不用补充

    //保存热敏打印的图片
    public static final int PER_SCREEN_DATA_SECOND = 10;
    public static final int CACHE_DATA_SECOND = 13;//内存中缓存的数据 13秒
    public static final int CACHE_DATA_SECOND_DATA = CACHE_DATA_SECOND * PER_SCREEN_DATA_SECOND;//内存中缓存的数据 s   13*10
    public static final boolean SHOW_NET_SPEED = false;//是否显示实时网速
    public static final int LOW_POWER_VALUE = 10;//低于百分之10
    public static final int LOW_POWER_VALUE_LOW = 5;//低于百分之5
    public static final int ID_INPUT_MAX_LEN = 20;
    public static final int FILTER_JUMP_DATA_COUNT = 200;
    public static final int MIN_CLICK_DELAY_TIME = 1000;
    public static final int PRINT_BOTTOM_TEXT_OFFSET_215 = 35;
    public static final int PRINT_BOTTOM_TEXT_OFFSET_210 = 30;
    public static final short ECG_DATA_HEADER_LEN = 1562;
    public static final short ECG_DATA_HEADER_LEN_CUSTOM_MY = 1562-100;
//    public static final int ANALYSIS_DIAGNOSIS_MODE = EcgDataAnalysisDiagnosisEnum.MODE_5000.getDiagnosisMode();
    public static final int FILE_POS_SAMPLE_SIZE = 21;
    public static final int FILE_POS_LEAD_SIZE = 25;
    public static final int FILE_POS_CRC = 4;
    public  static final int A4_WIDTH = 2100;
    public static final int A4_HEIGHT = 2970;
    public static boolean FILTER_ON = true;//滤波是否打开
    public static boolean FILTER_ON_HP = true;//高通滤波是否打开
    public static final boolean CAN_SWITCH_HIGH_FREQUENCY = false;//是否允许切换采样高频率
    public static final boolean EXPORT_HLT_FILTER_DATA = false;//是否导出滤波后的xml数据
    public static final boolean SAMPLE_SAVE_SRC_DATA = false;//是否采集保存原始数据
    public static final boolean PRINT_SERIAL_SEND_DEMO_DATA = false;//send demo data
    public static final int PRINT_SERIAL_SEND_DEMO_DATA_SIZE = 500;//500

    public static final boolean SUPPORT_ALL_LANGUAGE = true;//ce版本支持，中，英，西，3个语言。
    public static final boolean SUPPORT_ALL_WORKMODE = false;//ce版本，高级功能模式，还没做好，先屏蔽掉。
    public static final boolean USE_HEIGHT_CALC_ECG_GRID = false;//true height;false width 计算
    public static final int DRAW_RESAMPLE_COUNT = 500;//画图重采样 500
    public static final boolean DRAW_SCREEN_RESAMPLE = false;//画图重采样
//    public static final long BACKUP_DB_DAYS = DateUtil.DAY_MILLIS * 1;//每隔几天备份数据库的时间
    public static final boolean SAMPLE_SERIAL_SAMPLE = true;//7寸，10寸采集串口是否相同
    public static final boolean MANUAL_MODE_WRITE_DATA = false;//手动工作模式采集中，是否保存数据，方便采集更多的数据，调试数据是否有问题。
    public static final int HEART_RATE_DETECT_VALUE = 200;//心率检测阀值
    public static final int TEST_MAKE_RECORD_DEFAULT_COUNT = 1000;//后门生成测试记录的默认条数

    public static final boolean IF_OPEN_DEVICE_ONVIF = false;//是否打开智能入网通讯
    public static final boolean TEST_SHOW_THERMAL_IMAGE = false;//测试 热敏打印，主页显示图片
    public static final boolean TEST_SAVE_THERMAL_IMAGE = true;//测试 热敏打印，保存原始数据
    public static boolean Cjbupgrade_flag = false;


    public static class Observer {
        public static final String NOTIFY_UPDATE_SYSTME_TIME = "notify_update_system_time";
        public static final String NOTIFY_UPDATE_SIM_STATE = "notify_update_sim_state";

        public static final String NOTIFY_APP_STATUSBAR_MESSAGE = "notify_app_statusbar_message";
        public static final String NOTIFY_APP_STATUSBAR_NET_CHANGE = "notify_app_statusbar_net_change";
        public static final String NOTIFY_APP_STATUSBAR_WIFI_SIG = "notify_app_statusbar_wifi_sig";
        public static final String NOTIFY_APP_STATUSBAR_SIG = "notify_app_statusbar_sig";
        public static final String NOTIFY_APP_STATUSBAR_BLUETOOTH = "notify_app_statusbar_bluetooth";
        public static final String NOTIFY_APP_STATUSBAR_NET_SPEED = "notify_app_statusbar_net_speed";
        public static final String NOTIFY_APP_STATUSBAR_USB = "notify_app_statusbar_usb";
        public static final String NOTIFY_APP_STATUSBAR_USB_STORAGE = "notify_app_statusbar_usb_storage";
        public static final String NOTIFY_APP_STATUSBAR_BATTERY = "notify_app_statusbar_battery";
        public static final String NOTIFY_APP_STATUSBAR_BATTERY_CHARGING = "notify_app_statusbar_battery_charging";
        public static final String NOTIFY_APP_STATUSBAR_BATTERY_PRINT = "notify_app_statusbar_battery_print";

        public static final String NOTIFY_MAIN_BOTTOM_MENU = "notify_main_bottom_menu";//底部菜单更新
        public static final String NOTIFY_MAIN_ARCHIVES_UPDATE = "notify_main_archives_update";//档案更新
        public static final String NOTIFY_MAIN_HEART_RATE_UPDATE = "notify_main_heart_rate_update";//main 心率更新
        public static final String NOTIFY_MAIN_LEAD_SIGNAL_STATE_UPDATE = "notify_main_lead_signal_state_update";//导联信号状态更新
        public static final String NOTIFY_MAIN_LEAD_OFF_STATE_UPDATE = "notify_main_lead_off_state_update";//导联脱落状态更新
        public static final String NOTIFY_STORAGE_ITEM_CHANGE = "notify_storage_item_change";// 存储列表单个变化
        public static final String NOTIFY_STORAGE_LIST_CHANGE = "notify_storage_list_change";// 存储列表变化
        public static final String NOTIFY_STORAGE_CLOSE_DIALOG = "notify_storage_close_dialog";// 存储列表 关闭弹出框

        public static final String NOTIFY_PRINT_PAPER_LESS = "notify_print_paper_less";// 打印缺紙
        public static final String NOTIFY_PRINT_DOOR_CHECK = "notify_print_door_check";// 仓门检查
        public static final String NOTIFY_PRINT_HEAD_TEMPERATURE = "notify_print_head_temperature";// 热敏头温度

        public static final String NOTIFY_REMOTE_DIAGNOSIS_REPORT = "notify_remote_diagnosis_report";//远程诊断报告生成
        public static final String NOTIFY_PATIENT_CASE_TO_SEARCH = "notify_patient_case_to_search";//病例管理搜索
        public static final String NOTIFY_APPOINTMENT_TO_SEARCH = "notify_appointment_to_search";//预约搜索
        public static final String NOTIFY_PREVIEW_ARCHIVES_UPDATE = "notify_preview_archives_update";//预览档案更新
        public static final String NOTIFY_APPOINTMENT_ARCHIVES_UPDATE = "notify_appointment_archives_update";//预约档案更新
        public static final String NOTIFY_APPOINTMENT_ADD = "notify_appointment_add";//新增预约
        public static final String NOTIFY_PATIENT_INFO_SCAN_IDCARD = "notify_patient_info_scan_idcard";//患者信息扫描身份证
        public static final String NOTIFY_PATIENT_INFO_SCAN_PATIENT_ID = "notify_patient_info_scan_patient_id";//患者信息扫描患者ID
        public static final String NOTIFY_PATIENT_INFO_AGE_CHANGED = "notify_patient_info_age_changed";//患者信息出生日期与年龄同步
        public static final String NOTIFY_PATIENT_INFO_BIRTHDAY_CHANGED = "notify_patient_info_birthday_changed";//患者信息出生日期与年龄同步
        public static final String NOTIFY_PATIENT_CASE_SELECT_ALL = "notify_patient_case_select_all";//病例管理全选
        public static final String NOTIFY_PATIENT_CASE_ITEM_SELECT_CHANGE = "notify_patient_case_item_select_change"; //病例单条选择状态改变
        public static final String NOTIFY_PATIENT_ORDER_ITEM_SELECT_CHANGE = "notify_patient_order_item_select_change"; //预约单条选择状态改变
        public static final String NOTIFY_PATIENT_CASE_MODIFY_DIAGNOSIS = "notify_patient_case_modify_diagnosis";//病例管理预览修改诊断结果回调
        public static final String NOTIFY_PATIENT_CASE_MANAGEMENT_LIST_REFRESH = "notify_patient_case_management_list_refresh";//病例管理列表刷新
        public static final String NOTIFY_PRINT_PREVIEW_PAGE_PRINT_COMPLETED = "notify_print_preview_page_print_completed";//打印预览页面 打印完成
     //   public static final String NOTIFY_PRINT_PREVIEW_PAGE_PRINT_COMPLETED = "notify_print_preview_page_print_completed";//打印 病例管理页面 打印完成


        public static final String NOTIFY_MAIN_LANGUAGE_CHANGE = "notify_main_language_change";//国际化语言变更
        public static final String NOTIFY_SETTING_RESTORE_DEFAULT = "notify_setting_restore_default";//恢复默认设置
        public static final String NOTIFY_MAIN_CANCEL_REALTIME_SAMPLE_TASK = "notify_main_cancel_realtime_sample_task";//取消实时采样任务
        public static final String NOTIFY_USB_PRINT_DEVICE_UPDATE = "notify_usb_print_device_update";//usb 打印设备更新
        public static final String NOTIFY_LOAD_DATA_FAIL = "notify_load_data_fail"; //下载数据异常通用提示
        public static final String NOTIFY_PATIENT_INFO_UNIT_CHANGE = "notify_patient_info_unit_change";//患者身高体重单位改变时通知
        public static final String SYNC_APPOINTMENT_URGENT_STATUS = "sync_appointment_urgent_status";//同步预约加急状态
        public static final String RESTORE_THE_SETTINGS_BEFORE_THE_DEMO_MODE= "restore_the_settings_before_the_demo_mode";//恢复演示模式之前的设置

        public static final String CANCEL_PRINT = "cancel_print";
    }

    public static class Key {
        public static final int KEY_DOWN_1 = KeyEvent.KEYCODE_ENTER;//屏幕下方第一个按钮
    }

    public static class Broadcast {
        public static final String ACTION_SHOW_NAVI_BAR = "com.custom.show_navigationbar";//显示导航栏
        public static final String ACTION_HIDE_NAVI_BAR = "com.custom.hide_navigationbar";//隐藏导航栏
        public static final String ACTION_MTP = "lepu.android.intent.action.enable_usb";//mtp模式
        public static final String ACTION_POWER = "lepu.android.intent.action.disable_usb";//充电模式

        public static final String ACTION_MODE_HOST = "com.mid.usboff";//host模式
        public static final String ACTION_MODE_DEVICE = "com.mid.usbon";//device模式
    }

    public static class DialogSize {
        public static final float[] STORAGE_SEARCH_ARRAY = new float[]{0.8f,0.4f};
        public static final float[] COMMON_DIALOG_SIZE_ARRAY = new float[]{0.6f,0.4f};
        public static final float[] COMMON_DIALOG_SIZE_ARRAY_2 = new float[]{0.6f,0.6f};
    }

    public static class IpRoute {
        public static final String AI_ADDRESS = "192.168.231.210";
        public static final int AI_PORT = 12340;
    }

    public static class EditTextFilter {
        public static final String NUMBER = "0123456789";
        public static final String NUMBER_CHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        public static final String HTTP_ADDRSS = ".//:ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-";
        public static final String NOT_SUPOORT_CHAR_REG_EX = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？ ]"; ;
    }

    public interface SampleKeyListener {
        void onKeyDown(int keyType);
        void onKeyUp(int keyType);
    }

    //app
    //第一次安装
    public static final String CONFIG_APP_FIRST_USE = "appFirstUse";
    //升级使用
    public static final String CONFIG_APP_CURRENT_VERSION_CODE = "appCurrentVersionCode";
    public static final String CONFIG_APP_UPGRADE_USE = "appUpgradeUse";

    public static final String CONFIG_MENU_BEAN = "appConfigMenuBean";
    public static final String CONFIG_ARCHIVES_BEAN = "appConfigArchivesBean";
    public static final String CONFIG_LOGIN_USER_CONFIG = "appConfigLoginUserConfig";
    public static final String CONFIG_APP_VERSION = "appConfigAppVersion";
    public static final String CONFIG_APP_VERSION_NET_CONFIG_FIRST_AUTH = "appConfigAppVersionNetConfigFirstAuth";
    public static final String CONFIG_ARCHIVES_NUMBER_AUTO = "appConfigArchivesNumberAuto";
    public static final String CONFIG_STORAGE_SORT_TYPE = "appConfigStorageSortType";
    public static final String CONFIG_USE_BATTERY_ADC = "appConfigUseBatteryAdc";
    public static final String CONFIG_CURRENT_PRIVACY_PWD = "appConfigCurrentPrivacyPwd";
    public static final String CONFIG_SYSTEM_DEVICE_ID = "appConfigSystemDeviceID";
    public static final String CONFIG_LOGIN_USER_NAME = "appConfigLoginUsername";
    public static final String CONFIG_LOGIN_PASSWORD = "appConfigLoginPassword";
    public static final String CONFIG_LOGIN_REMEMBER_PASSWORD = "appConfigRememberPassword";
    public static final String CONFIG_LOGIN_LOCAL_PASSWORD = "appConfigLocalPassword";
    public static final String CONFIG_LOGIN_NET_ERROR_TIMES = "appConfigLoginNetErrorTimes";
    public static final String CONFIG_LOGIN_OVER_ERROR_TIMES_TIMESTAMP = "appConfigLoginOverErrorTimesTimeStamp";
    public static final String CONFIG_APP_LEAD_TYPE = "appConfigLeadType";//当前导联类型
    public static final String CONFIG_APP_USER_LIST = "appConfigLUserList";

    public static final String CONFIG_APP_AUTH_SCP = "appConfigAuthScp";
    public static final String CONFIG_APP_AUTH_DICOM = "appConfigAuthDicom";
    public static final String CONFIG_APP_AUTH_HL7 = "appConfigAuthHl7";
    public static final String CONFIG_APP_AUTH_GLASSGO = "appConfigGlassgo";


}
