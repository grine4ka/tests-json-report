//
//  BTests.swift
//  HalloFrischTests
//
//  Created by Everyone's Favorite iOS Developer on 07.12.22.
//

import XCTest
@testable import HalloFrisch

final class BTests: XCTestCase {

    func testExample1() {
        // Groundbreaking
        XCTAssertEqual(true, false)
    }

    func testExample2() {
        // oh wow
    }

    func testExample3() {
        // oh gosh

        let expectation = XCTestExpectation(description: "it works wow")

        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + 5) {
            expectation.fulfill()
        }

        wait(for: [expectation], timeout: 5.5)
    }

    func testSymbolFromMainProjectExistsHereHeckWhoKnowsWhatCouldGoWrong() {
        XCTAssertEqual(someCoolWord, "fresh")
    }
}
