import React from 'react';
import {NavigationContainer} from '@react-navigation/native'
import {createStackNavigator} from '@react-navigation/stack'
import MainScreen from './src/screens/MainScreen'

const Stack = createStackNavigator()
const App = () =>{
  return(
   <NavigationContainer>
     <Stack.Navigator>
       <Stack.Screen name="Main" component={MainScreen}/>
     </Stack.Navigator>
   </NavigationContainer>
  )
}
export default App;
