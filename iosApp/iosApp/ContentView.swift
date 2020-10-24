import SwiftUI
import shared

func greet() -> String {
    return Greeting().greeting()
}

func randomUUID() -> String {
    return Greeting().randomUUID()
}

struct ContentView: View {
    var body: some View {
        Text(greet())
        Text(randomUUID())
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
