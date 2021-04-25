package com.example.reportdemo;

import org.objectweb.asm.Opcodes;

import java.util.HashMap;

///
///@author jacky.peng on
///
///插桩配置信息类
class TrackHookConfig {
    /**
     * Fragment中的方法
     */
    public final static HashMap<String, TrackMethodCell> FRAGMENT_METHODS = new HashMap<>();
    public final static String TRACK_API = "com/example/TrackCenterHelper";

    static {
//        FRAGMENT_METHODS.put("onResume()V", new TrackMethodCell(
//                "onResume",
//                "()V",
//                "",// parent省略，均为 android/app/Fragment 或 android/support/v4/app/Fragment
//                "trackFragmentResume",
//                "(Ljava/lang/Object;)V",
//                0, 1, [Opcodes.ALOAD]))
//        FRAGMENT_METHODS.put("setUserVisibleHint(Z)V", new TrackMethodCell(
//                "setUserVisibleHint",
//                "(Z)V",
//                "",// parent省略，均为 android/app/Fragment 或 android/support/v4/app/Fragment
//                "trackFragmentSetUserVisibleHint",
//                "(Ljava/lang/Object;Z)V",
//                0, 2,
//                [Opcodes.ALOAD, Opcodes.ILOAD]))
//        FRAGMENT_METHODS.put("onHiddenChanged(Z)V", new TrackMethodCell(
//                "onHiddenChanged",
//                "(Z)V",
//                "",
//                "trackOnHiddenChanged",
//                "(Ljava/lang/Object;Z)V",
//                0, 2,
//                [Opcodes.ALOAD, Opcodes.ILOAD]))
        FRAGMENT_METHODS.put("onViewCreated(Landroid/view/View;Landroid/os/Bundle;)V", new TrackMethodCell(
                "onViewCreated",
                "(Landroid/view/View;Landroid/os/Bundle;)V",
                "",
                "trackOnFragmentViewCreated",
                "(Ljava/lang/Object;Landroid/view/View;Landroid/os/Bundle;)V",
                0, 3,
                [Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ALOAD]))
        FRAGMENT_METHODS.put("onDestroyView()V", new TrackMethodCell(
                "onDestroyView",
                "()V",
                "",
                "trackOnFragmentViewDestroyed",
                "(Ljava/lang/Object;)V",
                0, 1,
                [Opcodes.ALOAD]))
        FRAGMENT_METHODS.put("onDestroy()V", new TrackMethodCell(
                "onDestroy",
                "()V",
                "",
                "trackOnFragmentDestroyed",
                "(Ljava/lang/Object;)V",
                0, 1,
                [Opcodes.ALOAD]))
        FRAGMENT_METHODS.put("onCreate(Landroid/os/Bundle;)V", new TrackMethodCell(
                "onCreate",
                "(Landroid/os/Bundle;)V",
                "",
                "trackOnFragmentCreated",
                "(Ljava/lang/Object;Landroid/os/Bundle;)V",
                0, 2,
                [Opcodes.ALOAD, Opcodes.ALOAD]))
        FRAGMENT_METHODS.put("onAttach(Landroid/content/Context;)V", new TrackMethodCell(
                "onAttach",
                "(Landroid/content/Context;)V",
                "",
                "trackOnFragmentAttached",
                "(Ljava/lang/Object;)V",
                0, 1,
                [Opcodes.ALOAD]))
        FRAGMENT_METHODS.put("onDetach()V", new TrackMethodCell(
                "onDetach",
                "()V",
                "",
                "trackOnFragmentDetached",
                "(Ljava/lang/Object;)V",
                0, 1,
                [Opcodes.ALOAD]))
    }
}
