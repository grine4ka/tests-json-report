//
//  ContentView.swift
//  HalloFrisch
//
//  Created by Everyone's Favorite iOS Developer on 07.12.22.
//

import SwiftUI

struct ContentView: View {
    var body: some View {
        VStack {
            Text(someCoolWord)
        }
        .font(.largeTitle)
        .fontWeight(.heavy)
        .monospaced()
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .foregroundColor(.white)
        .background(Color(red: 0.208, green: 0.471, blue: 0.294))
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}

let someCoolWord = "fresh"
