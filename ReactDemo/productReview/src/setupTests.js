import "jest-enzyme";
import { configure } from "enzyme";
import Adapter from "@wojtekmaj/enzyme-adapter-react-17";
import jestFetchMock from 'jest-fetch-mock';

configure({ adapter: new Adapter() });
global.fetch = jestFetchMock;