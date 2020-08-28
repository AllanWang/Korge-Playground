import com.soywiz.korge.gradle.*

buildscript {
	val korgePluginVersion: String by project

	repositories {
		mavenLocal()
		maven { url = uri("https://dl.bintray.com/korlibs/korlibs") }
		maven { url = uri("https://plugins.gradle.org/m2/") }
		mavenCentral()
		google()
	}
	dependencies {
		classpath("com.soywiz.korlibs.korge.plugins:korge-gradle-plugin:$korgePluginVersion")
	}
}

apply<KorgeGradlePlugin>()

korge {
	// See https://korlibs.soywiz.com/korge/setup/gradle-plugin/#the-korge-extension
	id = "com.sample.demo"

	version = "0.0.1"
	exeBaseName = "app"
	name = "unnamed"
	description = "description"
	orientation = Orientation.DEFAULT
	copyright = "Copyright (c) 2020 Unknown"

	// Configuring the author
	authorName = "unknown"
	authorEmail = "unknown@unknown"
	authorHref = "http://localhost"
	author("name", "email", "href")

	icon = File(rootDir, "icon.png")

	gameCategory = GameCategory.ACTION
	fullscreen = false
//	backgroundColor = 0xff000000.toInt()
	entryPoint = "main"
	jvmMainClassName = "MainKt"
//	androidMinSdk = null
}
