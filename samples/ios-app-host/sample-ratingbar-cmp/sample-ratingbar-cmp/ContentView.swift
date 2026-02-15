import SwiftUI
import UIKit
import RatingBarSample

struct ContentView: View {
    var body: some View {
        RatingBarComposeView()
            .ignoresSafeArea()
    }
}

private struct RatingBarComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}

#Preview {
    ContentView()
}
