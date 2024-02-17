import {
  NativeModules,
  Platform,
  type EmitterSubscription,
} from 'react-native';
import { useEffect, useState } from 'react';
import { DeviceEventEmitter } from 'react-native';

const LINKING_ERROR =
  `The package 'eventique' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const Eventique = NativeModules.Eventique
  ? NativeModules.Eventique
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export const useEventiQueListener = () => {
  const [message, setMessage] = useState(null);
  const [isConnected, setIsConnected] = useState(false);

  useEffect(() => {
    let messageListener: EmitterSubscription;

    const connectToEventQueue = async () => {
      try {
        await Eventique.connectToEventQueue(null);
        setIsConnected(true);

        messageListener = DeviceEventEmitter.addListener(
          'sync_event',
          (event) => {
            console.log(
              `Received message: ${event.message} from topic: ${event.topic}`
            );
            setMessage(event.message);
          }
        );
      } catch (error) {
        console.log(error);
      }
    };

    connectToEventQueue();

    return () => {
      if (messageListener) {
        messageListener.remove();
      }
    };
  }, []);

  return [message, isConnected];
};
