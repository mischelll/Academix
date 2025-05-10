import { Outlet } from "react-router-dom";
import CurriculumNavigator from "./CurriculumNavigator";

export default function CurriculumLayout() {
  return (
    <div className="p-6">
      <CurriculumNavigator />
      <div className="mt-4">
        <Outlet /> {/* This renders the nested route component */}
      </div>
    </div>
  );
}