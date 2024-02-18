import * as React from 'react';

import { StyleSheet, View, Text } from 'react-native';
import { useEventiQueListener } from 'eventique';

export default function App() {
  const [message, isConnected] = useEventiQueListener({apiKey:"xxxx"});
  console.log('message: ', message);
  console.log('isConnected: ', isConnected);

  return (
    <View style={styles.container}>
      <Text>Action: {message}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
