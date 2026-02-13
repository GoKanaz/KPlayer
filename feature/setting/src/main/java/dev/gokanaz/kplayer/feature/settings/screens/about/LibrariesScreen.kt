package dev.gokanaz.kplayer.feature.settings.screens.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.GitHub
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Library(
    val name: String,
    val version: String,
    val license: String,
    val licenseType: String,
    val repository: String,
    val description: String,
    val author: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibrariesScreen(
    onNavigateBack: () -> Unit,
    onOpenLink: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var expandedLicense by remember { mutableStateOf<String?>(null) }
    
    val libraries = remember { getLibraries() }
    val filteredLibraries = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            libraries
        } else {
            libraries.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                it.license.contains(searchQuery, ignoreCase = true) ||
                it.licenseType.contains(searchQuery, ignoreCase = true)
            }
        }
    }
    
    val groupedByLicense = remember(filteredLibraries) {
        filteredLibraries.groupBy { it.licenseType }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Open Source Libraries") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search libraries...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                singleLine = true
            )
            
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                groupedByLicense.forEach { (licenseType, libraries) ->
                    item {
                        Text(
                            text = licenseType,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    
                    items(libraries) { library ->
                        LibraryItem(
                            library = library,
                            isExpanded = expandedLicense == library.name,
                            onToggleExpand = {
                                expandedLicense = if (expandedLicense == library.name) null else library.name
                            },
                            onOpenLink = onOpenLink
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LibraryItem(
    library: Library,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onOpenLink: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        onClick = onToggleExpand
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = library.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Text(
                        text = "${library.version} • ${library.license}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Icon(
                    if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null
                )
            }
            
            if (isExpanded) {
                Spacer(modifier = Modifier.height(12.dp))
                
                HorizontalDivider()
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = library.description,
                    fontSize = 14.sp
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Author: ${library.author}",
                    fontSize = 14.sp
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IconButton(
                        onClick = { onOpenLink(library.repository) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.GitHub, contentDescription = "Repository")
                    }
                    
                    IconButton(
                        onClick = { onOpenLink("https://opensource.org/licenses/${library.license}") },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.Info, contentDescription = "License Info")
                    }
                    
                    IconButton(
                        onClick = { onOpenLink(library.repository) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.Link, contentDescription = "Website")
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Full License Text",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable { onOpenLink("https://opensource.org/licenses/${library.license}") }
                        .padding(vertical = 4.dp)
                )
            }
        }
    }
}

private fun getLibraries(): List<Library> {
    return listOf(
        Library(
            name = "AndroidX Core",
            version = "1.12.0",
            license = "Apache 2.0",
            licenseType = "Apache License 2.0",
            repository = "https://developer.android.com/jetpack/androidx",
            description = "The AndroidX Core library provides backward-compatible versions of Android framework APIs.",
            author = "Google"
        ),
        Library(
            name = "Jetpack Compose",
            version = "1.5.4",
            license = "Apache 2.0",
            licenseType = "Apache License 2.0",
            repository = "https://developer.android.com/jetpack/compose",
            description = "Jetpack Compose is Android's modern toolkit for building native UI.",
            author = "Google"
        ),
        Library(
            name = "Kotlin Coroutines",
            version = "1.7.3",
            license = "Apache 2.0",
            licenseType = "Apache License 2.0",
            repository = "https://github.com/Kotlin/kotlinx.coroutines",
            description = "Library support for Kotlin coroutines.",
            author = "JetBrains"
        ),
        Library(
            name = "Dagger Hilt",
            version = "2.48",
            license = "Apache 2.0",
            licenseType = "Apache License 2.0",
            repository = "https://dagger.dev/hilt/",
            description = "Dagger's Android integration for dependency injection.",
            author = "Google"
        ),
        Library(
            name = "DataStore",
            version = "1.0.0",
            license = "Apache 2.0",
            licenseType = "Apache License 2.0",
            repository = "https://developer.android.com/topic/libraries/architecture/datastore",
            description = "Jetpack DataStore is a data storage solution.",
            author = "Google"
        ),
        Library(
            name = "Protobuf Lite",
            version = "3.25.1",
            license = "BSD 3-Clause",
            licenseType = "BSD License",
            repository = "https://github.com/protocolbuffers/protobuf",
            description = "Protocol Buffers - Google's data interchange format.",
            author = "Google"
        ),
        Library(
            name = "Material 3",
            version = "1.1.2",
            license = "Apache 2.0",
            licenseType = "Apache License 2.0",
            repository = "https://github.com/material-components/material-components-android",
            description = "Material Components for Android.",
            author = "Google"
        ),
        Library(
            name = "Media3",
            version = "1.1.1",
            license = "Apache 2.0",
            licenseType = "Apache License 2.0",
            repository = "https://developer.android.com/media/media3",
            description = "Android's media library for audio/video playback.",
            author = "Google"
        ),
        Library(
            name = "Coil",
            version = "2.5.0",
            license = "Apache 2.0",
            licenseType = "Apache License 2.0",
            repository = "https://coil-kt.github.io/coil/",
            description = "Image loading library for Android backed by Kotlin Coroutines.",
            author = "Coil Contributors"
        ),
        Library(
            name = "MockK",
            version = "1.13.8",
            license = "Apache 2.0",
            licenseType = "Apache License 2.0",
            repository = "https://mockk.io/",
            description = "Mocking library for Kotlin.",
            author = "MockK Team"
        ),
        Library(
            name = "JUnit",
            version = "4.13.2",
            license = "EPL 1.0",
            licenseType = "Eclipse Public License",
            repository = "https://junit.org/junit4/",
            description = "Unit testing framework for Java.",
            author = "JUnit Team"
        )
    )
}
