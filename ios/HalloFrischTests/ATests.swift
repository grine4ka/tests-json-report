//
//  ATests.swift
//  HalloFrischTests
//
//  Created by Everyone's Favorite iOS Developer on 07.12.22.
//

import XCTest

final class ATests: XCTestCase {

    func testExample1() {
        // Groundbreaking
        XCTAssertEqual(true, true)
    }

    func testExample2() {
        // oh wow
    }

    func testExample3() {
        // oh gosh

        let expectation = XCTestExpectation(description: "it works wow")
        expectation.fulfill()

        wait(for: [expectation], timeout: 0.5)
    }

    func testExample4() throws {
        // golly
        XCTAssertEqual(2, 5 % 3)
    }

    func testExample5() throws {
        // oh no
        XCTFail("this one doesn't work but that's kinda expected")
    }

    func testExample6() throws {
        // really up to no good here

        if Int.random(in: 0..<10) % 3 == 0 {
            XCTFail("wow what an awful test that sometimes fails hope nobody would ever do that")
        }
    }
}
