import { Tabs, TabsList, TabsTrigger, TabsContent } from '../components/ui/tabs';

export default function Home() {
  return (
    <Tabs defaultValue="submitted" className="w-[400px]">
      <TabsList>
        <TabsTrigger value="submitted">Submitted</TabsTrigger>
        <TabsTrigger value="unsubmitted">Unsubmitted</TabsTrigger>
      </TabsList>
      <TabsContent value="submitted">
        Your submitted assignments
      </TabsContent>
      <TabsContent value="unsubmitted">Here are your unsubmitted assignments</TabsContent>
    </Tabs>
  );
}
