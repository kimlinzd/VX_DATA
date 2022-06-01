LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE_TAGS := samples
LOCAL_MODULE    := libjniUart
LOCAL_SRC_FILES := \
  jniUart.c \
  ringBuffer.c

LOCAL_LDLIBS :=-llog
LOCAL_CFLAGS += -Wno-error=date-time
LOCAL_CFLAGS += -Wno-date-time \
           -Wno-unused-const-variable \
           -Wno-unused-variable \
          -Wno-unused-parameter \
          -Wno-implicit-fallthrough \
          -Wno-missing-field-initializers \
          -Wno-format \
          -Wno-unused-function \
          -Wno-unused-value \
          -Wno-constant-logical-operand \
          -Wno-incompatible-pointer-types \
          -Wno-switch-bool \
          -Wno-int-conversion \
		  -Wno-pointer-sign \
    
include $(BUILD_SHARED_LIBRARY)