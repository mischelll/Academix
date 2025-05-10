import { useParams, Link } from "react-router-dom";
import {
  Breadcrumb,
  BreadcrumbList,
  BreadcrumbItem,
  BreadcrumbLink,
  BreadcrumbSeparator,
} from "../ui/breadcrumb";
import Semester from "./Semester";

function CurriculumNavigator() {
    const { majorId, semesterId, courseId } = useParams();

    return (
      <Breadcrumb>
        <BreadcrumbList>
          <BreadcrumbItem>
            <BreadcrumbLink asChild>
            <Link to="/curriculum/majors">Majors</Link>
            </BreadcrumbLink>
          </BreadcrumbItem>
  
          {majorId && (
            <>
              <BreadcrumbSeparator />
              <BreadcrumbItem>
                <BreadcrumbLink asChild>
                  <Link to={`/curriculum/majors/${majorId}/semesters`}><Semester/></Link>
                </BreadcrumbLink>
              </BreadcrumbItem>
            </>
          )}
  
          {semesterId && (
            <>
              <BreadcrumbSeparator />
              <BreadcrumbItem>
                <BreadcrumbLink asChild>
                  <Link to={`/curriculum/semesters/${semesterId}/courses`}>Courses</Link>
                </BreadcrumbLink>
              </BreadcrumbItem>
            </>
          )}
  
          {courseId && (
            <>
              <BreadcrumbSeparator />
              <BreadcrumbItem>
                <BreadcrumbLink asChild>
                  <Link to={`/curriculum/courses/${courseId}/lessons`}>Lessons</Link>
                </BreadcrumbLink>
              </BreadcrumbItem>
            </>
          )}
        </BreadcrumbList>
      </Breadcrumb>
    );
}

export default CurriculumNavigator;
