package com.rsupport.rv.viewer.sdk.data.packet

import com.rsupport.rv.viewer.sdk.common.log.RLog
import com.rsupport.rv.viewer.sdk.decorder.model.SystemInfo
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.Charset

class SystemInfo {
    private var rootNode: SystemInfoNode? = null

    constructor(data: ByteArray, length: Int) {
        var idx = 0
        var type: Int
        var parentKey: Int
        var key: Int
        var existList: Int
        var iconIndex: Int
        var len: Int
        var text: String
        var columnCount: Int
        var valueCount: Int

        var columnKey: String? = null

        mainloop@while((idx + 4) < length) {
            type = ByteBuffer.wrap(data, idx, 4).order(ByteOrder.LITTLE_ENDIAN).int; idx+=4

            if(type == 0) {

                if ((idx + 4) >= length) continue
                parentKey = ByteBuffer.wrap(data, idx, 4).order(ByteOrder.LITTLE_ENDIAN).int; idx+=4

                if ((idx + 4) >= length) continue
                key = ByteBuffer.wrap(data, idx, 4).order(ByteOrder.LITTLE_ENDIAN).int; idx+=4

                if ((idx + 4) >= length) continue
                existList = ByteBuffer.wrap(data, idx, 4).order(ByteOrder.LITTLE_ENDIAN).int; idx+=4

                if ((idx + 4) >= length) continue
                iconIndex = ByteBuffer.wrap(data, idx, 4).order(ByteOrder.LITTLE_ENDIAN).int; idx+=4

                if ((idx + 4) >= length) continue
                len = ByteBuffer.wrap(data, idx, 4).order(ByteOrder.LITTLE_ENDIAN).int; idx+=4

                if ((idx + len) >= length) continue
                text = String(data, idx, len, Charset.forName("EUC-KR")); idx+=len

                if(parentKey == -1) {
                    rootNode = SystemInfoNode(null, key, (existList==1), iconIndex, text)
                }else {
                    val parentNode = rootNode?.findNodeByKey(parentKey)
                    if(parentNode == null) {
                        RLog.d("SystemInfo: Cannot find parent node for parentKey=$parentKey, key=$key, existList=$existList, iconIndex=$iconIndex, text=$text")
                    }else {
                        parentNode.addChildNode(SystemInfoNode(parentNode, key, (existList==1), iconIndex, text))
                    }
                }
            }else if(type == 1) {
                if((idx + 4) >= length) continue
                parentKey = ByteBuffer.wrap(data, idx, 4).order(ByteOrder.LITTLE_ENDIAN).int; idx+=4

                if((idx + 4) >= length) continue
                columnCount = ByteBuffer.wrap(data, idx, 4).order(ByteOrder.LITTLE_ENDIAN).int; idx+=4

                val parentNode = rootNode?.findNodeByKey(parentKey)

                for(i in 0 until columnCount) {
                    if((idx + 4) >= length) break
                    len = ByteBuffer.wrap(data, idx, 4).order(ByteOrder.LITTLE_ENDIAN).int; idx+=4

                    if((idx + len) >= length) break
                    text = String(data, idx, len, Charset.forName("EUC-KR")); idx+=len
                }

                if(columnCount != 2) {
                    RLog.w("SystemInfo: Column count != 2. Ignore node. count=$columnCount")
                }

                if((idx + 4) >= length) continue
                valueCount = ByteBuffer.wrap(data, idx, 4).order(ByteOrder.LITTLE_ENDIAN).int; idx+=4

                val rowCount = valueCount * columnCount

                for(i in 0 until rowCount) {
                    if((idx + 4) >= length) break
                    len = ByteBuffer.wrap(data, idx, 4).order(ByteOrder.LITTLE_ENDIAN).int; idx+=4

                    if((idx + len) >= length) break
                    text = String(data, idx, len, Charset.forName("EUC-KR")); idx+=len

                    if(columnKey == null) {
                        columnKey = text
                    }else {
                        parentNode?.addRow(columnKey, text)
                        columnKey = null
                    }
                }

                if(columnKey != null) {
                    parentNode?.addRow(columnKey, "")
                }
            }
        }
    }


    fun toLegacyObject(): SystemInfo {
        val info = SystemInfo()
        info.machineName = rootNode?.findFirstTableItem("Machine Name")
        info.systemModel = rootNode?.findFirstTableItem("System Model")
        info.systemManufacturer = rootNode?.findFirstTableItem("System Manufacturer")
        info.cpu = rootNode?.findFirstTableItem("CPU")
        info.cpuArchitecturer = rootNode?.findFirstTableItem("CPU Architecture")
        info.memory = rootNode?.findFirstTableItem("Memory")
        info.productName = rootNode?.findFirstTableItem("Product Name")
        info.productVersion = rootNode?.findFirstTableItem("Product Version")
        info.productID = rootNode?.findFirstTableItem("Product ID")
        info.winPlatform = rootNode?.findFirstTableItem("WinPlatform")
        info.localAddress = rootNode?.findFirstTableItem("Local Address")
        info.macAddress = rootNode?.findFirstTableItem("MAC Address")
        info.ieVersion = rootNode?.findFirstTableItem("IE Version")
        info.regUsername = rootNode?.findFirstTableItem("Register UserName")
        info.regOrgname = rootNode?.findFirstTableItem("Register OrgName")
        info.systemUptime = rootNode?.findFirstTableItem("System-Up Time")
        info.bootUptime = rootNode?.findFirstTableItem("Boot-Up Time")
        info.winsockVersion = rootNode?.findFirstTableItem("WinSock Version")
        info.winsockDescription = rootNode?.findFirstTableItem("WinSock Description")

        return info

    }


    override fun toString(): String {
        return rootNode?.toString() ?: "null"
    }

    data class SystemInfoNode(
        val parentNode: SystemInfoNode?,
        val key: Int,
        val hasList: Boolean,
        val iconIndex: Int,
        val name: String
    ) {
        private var childs = ArrayList<SystemInfoNode>()
        private var table = HashMap<String, String>()

        fun findNodeByKey(key: Int): SystemInfoNode? {
            if(this.key == key) {
                return this
            }else {
                for(c in childs) {
                    val node = c.findNodeByKey(key)
                    if(node != null) {
                        return node
                    }
                }
            }

            return null
        }

        fun addChildNode(node: SystemInfoNode) {
            childs.add(node)
        }

        fun addRow(name: String, value: String) {
            table.put(name, value)
        }

        fun findFirstTableItem(name: String): String {
            for((k, v) in table) {
                if(name.equals(k)) {
                    return v
                }
            }

            for(c in childs) {
                return c.findFirstTableItem(name)
            }


            //Not found...
            return ""
        }



        override fun toString(): String {
            var str = StringBuffer()
            str.append("[${this.name}] ")
                    .append("parent=${parentNode?.name}, key=$key, iconIndex=$iconIndex, childs=${childs.count()}")
            str.append("\n").append(table.toString())

            for(c in childs) {
                str.append("\n").append(c.toString())
            }

            return str.toString()
        }
    }
}