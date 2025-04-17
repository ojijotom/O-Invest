package com.ojijo.o_invest.ui.screens.profile

@Composable
fun ProfileScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(24.dp)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
        ) {
            // User Info Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(64.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("John Doe", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text("johndoe@email.com", color = Color.Gray)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Settings Button
            Button(
                onClick = { /* navigate to settings */ },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Edit Profile")
            }
        }
    }
}
