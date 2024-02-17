
# React Native Eventique Module

The React Native Eventique Module is a lightweight and efficient solution for performing actions in your app based on server events, eliminating the need for polling and reducing server resource usage. With just a simple hook, you can send actions from your server to dictate what your app should do. This module is currently implemented for Android only.

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
