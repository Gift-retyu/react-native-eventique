
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNEventiqueSpec.h"

@interface Eventique : NSObject <NativeEventiqueSpec>
#else
#import <React/RCTBridgeModule.h>

@interface Eventique : NSObject <RCTBridgeModule>
#endif

@end
