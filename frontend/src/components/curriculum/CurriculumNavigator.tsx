import { useParams, Link } from "react-router-dom";
import {
  Breadcrumb,
  BreadcrumbList,
  BreadcrumbItem,
  BreadcrumbLink,
  BreadcrumbSeparator,
} from "../ui/breadcrumb";

function CurriculumNavigator() {
    const { majorId, semesterId, courseId } = useParams();

    const breadcrumbs = [{ label: "Majors", to: "/curriculum/majors" }];

    if (majorId) {
      breadcrumbs.push({
        label: `Major ${majorId}`,
        to: `/curriculum/majors/${majorId}`
      });
      breadcrumbs.push({
        label: "Semesters",
        to: `/curriculum/majors/${majorId}/semesters`
      });
    }

    if (semesterId) {
      breadcrumbs.push({
        label: `Semester ${semesterId}`,
        to: `/curriculum/semesters/${semesterId}/courses`
      });
    }

    if (courseId) {
      breadcrumbs.push({
        label: `Course ${courseId}`,
        to: `/curriculum/courses/${courseId}/lessons`
      });
    }

    return (
      <div className="overflow-x-auto whitespace-nowrap">
        <Breadcrumb>
          <BreadcrumbList>
            {breadcrumbs.map((crumb, index) => (
              <BreadcrumbItem key={crumb.to}>
                <BreadcrumbLink asChild>
                  <Link to={crumb.to}>{crumb.label}</Link>
                </BreadcrumbLink>
                {index < breadcrumbs.length - 1 && <BreadcrumbSeparator />}
              </BreadcrumbItem>
            ))}
          </BreadcrumbList>
        </Breadcrumb>
      </div>
    );
}

export default CurriculumNavigator;
