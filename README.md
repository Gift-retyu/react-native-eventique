
# React Native Eventique Module

The React Native Eventique Module is a revolutionary tool that transforms the way your app interacts with server events. Designed for Android, this lightweight and efficient module eliminates the need for constant polling, significantly reducing server resource consumption. By integrating with the [eventique-push](https://www.npmjs.com/package/eventique-push) library on your server, you can effortlessly push actions to your device, enabling it to perform a wide range of tasks.

Imagine the convenience of remotely clearing a shopping cart, prompting your app to fetch the latest updates, or initiating any number of custom actions - the possibilities are truly exciting! With the Eventique Module, your app becomes a dynamic and responsive entity, capable of adapting to your server's commands in real time.

## Getting Started

### Installation

To install the Eventique module, run the following command in your React Native project:

```bash
npm install react-native-eventique
```

or if you use Yarn:

```bash
yarn add react-native-eventique
```

### Setup

1. Import the `useEventiQueListener` hook from the Eventique module:

   ```javascript
   import { useEventiQueListener } from 'react-native-eventique';
   ```

### Usage

To use the Eventique hook in your app, follow these steps:

1. Use the `useEventiQueListener` hook in your component to listen for messages and connection status:

   ```javascript
   const [message, isConnected] = useEventiQueListener();

   console.log('Message: ', message);
   console.log('Is Connected: ', isConnected);
   ```

2. Display the received message in your component:

   ```javascript
   return (
     <View style={styles.container}>
       <Text>Action: {message}</Text>
     </View>
   );
   ```

## Example

Here's a simple example of how to use the Eventique module in a React Native app:

```javascript
import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { useEventiQueListener } from 'react-native-eventique';

const App = () => {
  const [message, isConnected] = useEventiQueListener();

  console.log('Message: ', message);
  console.log('Is Connected: ', isConnected);

  return (
    <View style={styles.container}>
      <Text>Action: {message}</Text>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
});

export default App;
```

## Support

For now, the Eventique module is only available for Android. If you encounter any issues or have feature requests, please feel free to open an issue on the [GitHub repository](https://github.com/Gift-retyu/react-native-eventique).

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
