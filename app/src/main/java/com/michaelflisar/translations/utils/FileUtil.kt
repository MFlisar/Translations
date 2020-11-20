package com.michaelflisar.translations.utils

import com.google.gson.Gson
import com.michaelflisar.translations.classes.ResString
import org.w3c.dom.Node
import org.w3c.dom.ls.DOMImplementationLS
import org.w3c.dom.ls.LSSerializer
import java.io.File
import java.io.StringWriter
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult


object FileUtil {

    fun listAllFiles(folder: File): List<File> {
        val allFiles = ArrayList<File>()
        val folders = folder.listFiles()?.toList() ?: emptyList()
        folders.forEach {
            if (it.isFile)
                allFiles.add(it)
            else
                allFiles.addAll(listAllFiles(it))
        }
        return allFiles
    }

    fun copyIfNotExists(fileFrom: File, fileTo: File): Boolean {
        return if (!fileTo.exists()) {
            fileTo.parentFile.mkdirs()
            Files.copy(fileFrom.toPath(), fileTo.toPath(), StandardCopyOption.REPLACE_EXISTING)
            true
        } else false
    }

    inline fun <reified T> readFile(file: File): T? {
        return if (file.exists()) {
            val bufferedReader = file.bufferedReader()
            val settingsString = bufferedReader.use { it.readText() }
            Gson().fromJson(settingsString, T::class.java)
        } else {
            null
        }
    }

    fun readStringResourceFile(file: File): List<ResString> {

        if (!file.exists())
            return emptyList()

        val strings = ArrayList<ResString>()
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        val document = builder.parse(file)
        val serializer = (document.implementation as DOMImplementationLS).createLSSerializer().apply {
            domConfig.setParameter("xml-declaration", false)
        }
//        val transformer = TransformerFactory.newInstance().newTransformer().apply {
//            setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no")
//            setOutputProperty(OutputKeys.CDATA_SECTION_ELEMENTS, "script");
//        }

        // <resource>
        val root = document.documentElement

        // <string>
        val stringNodes = root.getElementsByTagName("string")
        for (i in 0 until stringNodes.length) {
            val node = stringNodes.item(i)
            val nameAttribute = node.attributes.getNamedItem("name")
            val translatableAttribute = node.attributes.getNamedItem("translatable")
            if (translatableAttribute == null || translatableAttribute.nodeValue == "true")
                strings.add(ResString(nameAttribute.nodeValue, innerXml(serializer, node)))
        }
        return strings
    }

    fun innerXml(serializer: LSSerializer, node: Node): String {
        val childNodes = node.childNodes
        val sb = StringBuilder()
        for (i in 0 until childNodes.length) {
            sb.append(serializer.writeToString(childNodes.item(i)))
        }
        var text = sb.toString()
        if (text.startsWith("<![CDATA["))
            text += "]]>"
        return text
    }

//    private fun getNodeContent(transformer: Transformer, node: Node): String {
//        val sw = StringWriter()
//        try {
//            transformer.transform(DOMSource(node), StreamResult(sw))
//        } catch (e: TransformerException) {
//            L.e("nodeToString Transformer Exception")
//            L.e(e)
//        }
//        return sw.toString()
//    }

    fun writeStringResourceFile(
        target: File,
        finalTranslatedString: java.util.ArrayList<ResString>
    ) {
        target.bufferedWriter().use { out ->
            out.appendLine("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
            out.appendLine("<resources>")
            finalTranslatedString.forEach {
                it.append(out, 1)
            }
            out.appendLine("</resources>")

        }
    }
}