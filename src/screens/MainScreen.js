import React, { useState, useEffect } from 'react'
import {
    View, Text, StyleSheet, Button, FlatList,
    NativeModules, TouchableOpacity, NativeEventEmitter
} from 'react-native'

const MainScreen = () => {
    const [deviceList, setDeviceList] = useState([])
    const { ConnectSDKNative } = NativeModules
    const eventEmitter = new NativeEventEmitter(ConnectSDKNative)

    //Callbacks
    const discoverDeviceHandler = () => {
            ConnectSDKNative.callDeviceDiscovery(
            (success) => {
                console.log(success)
            },
            (error) => {
                console.log(error)
            })
    }

    // //Promises
    // const discoverDeviceHandler = async () => {
    //     const message = await ConnectSDKNative.callDeviceDiscovery("Hi from JS World")
    //     console.log(message)
    // }

    useEffect(() => {
        const newDevice = eventEmitter.addListener(
            'DeviceFound',
            (deviceDiscovered) => {
                console.log(deviceDiscovered.friendlyName)
                setDeviceList(deviceList => [...deviceList,deviceDiscovered])
            }
        )
        return () => newDevice.remove()
    }, [])

    const renderListItem = (itemData) => {
        return (
            <TouchableOpacity>
                <View style={styles.listContainer}>
                    <Text>{itemData.item.friendlyName}</Text>
                    <Text>{itemData.item.ipAddress}</Text>
                </View>
            </TouchableOpacity>
        )
    }
    return (
        <View style={styles.mainContainer}>
            <View style={styles.button}>
                <Button title="Discover Devices" onPress={discoverDeviceHandler} />
            </View>
            <FlatList
                style={styles.list}
                keyExtractor={(item, index) => item.id}
                renderItem={renderListItem}
                data={deviceList}
            />
        </View>
    )
}

const styles = StyleSheet.create({
    mainContainer: {
        flex: 1,
        alignItems: 'center'
    },
    button: {
        height: 50,
        width: '90%',
        marginTop: 20
    },
    listContainer: {
        flexDirection: 'row',
        height: 50,
        borderWidth: 0.5,
        borderColor: 'black',
        marginTop: 10,
        alignItems: 'center',
        justifyContent: 'space-around'
    },
    list: {
        width: '90%'
    }
})

export default MainScreen