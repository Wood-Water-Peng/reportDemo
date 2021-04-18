import org.objectweb.asm.Opcodes

class TrackUtil {
    public static final int ASM_VERSION = Opcodes.ASM6
    private static final HashSet<String> targetFragmentClass = new HashSet()

    public static final HashMap<String, String> childAndParents = new HashMap<>()
    static {
        /**
         * For Android App Fragment
         */
        targetFragmentClass.add('android/app/Fragment')
        targetFragmentClass.add('android/app/ListFragment')
        targetFragmentClass.add('android/app/DialogFragment')

        /**
         * For Support V4 Fragment
         */
        targetFragmentClass.add('android/support/v4/app/Fragment')
        targetFragmentClass.add('android/support/v4/app/ListFragment')
        targetFragmentClass.add('android/support/v4/app/DialogFragment')

        /**
         * For AndroidX Fragment
         */
        targetFragmentClass.add('androidx/fragment/app/Fragment')
        targetFragmentClass.add('androidx/fragment/app/ListFragment')
        targetFragmentClass.add('androidx/fragment/app/DialogFragment')
    }

    static boolean isPublic(int access) {
        return (access & Opcodes.ACC_PUBLIC) != 0
    }

    static boolean isStatic(int access) {
        return (access & Opcodes.ACC_STATIC) != 0
    }

    static boolean isProtected(int access) {
        return (access & Opcodes.ACC_PROTECTED) != 0
    }

    static String findTopParent(String child) {
        String result;
        while (childAndParents.containsKey(child)) {
            result = childAndParents.get(child)
            child = result
        }
        return result;
    }

    static boolean isInstanceOfFragment(String superName) {
        return targetFragmentClass.contains(superName)
    }
}