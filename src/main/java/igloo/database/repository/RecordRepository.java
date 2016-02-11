package igloo.database.repository;

import igloo.database.entity.RecordEntity;
import igloo.rest.domain.OperationHoursPerMonth;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Yikai Gong
 */
public interface RecordRepository extends CrudRepository<RecordEntity, Integer> {
    @Query("select r from RecordEntity r")
    List<RecordEntity> findAllRecords();

    @Query("select r from RecordEntity r where r.serial_number=?1 order by r.timestamp desc")
    List<RecordEntity> findRecordsBySerialNum(String serialNum);

    @Query("select r from RecordEntity r where r.chip_id=?1 order by r.timestamp desc")
    List<RecordEntity> findRecordsByChipId(String chipId);

    @Modifying
    @Transactional
    @Query("delete from RecordEntity r where r.chip_id=?1")
    void deleteRecordsByChipId(String chipId);

    @Query("select distinct r.serial_number, r.latitude, r.longitude from RecordEntity r")
    List<RecordEntity> findAllDevices();

    @Query("select distinct r.chip_id, r.latitude, r.longitude, r.suburb from RecordEntity r where r.latitude > ?1 and r.latitude < ?2 and r.longitude > ?3 and r.longitude < ?4")
    List<RecordEntity> findAllDevicesByBoundingBox(Double latiMin, Double latiMax, Double lonMin, Double lonMax);

    @Query("select new igloo.rest.domain.OperationHoursPerMonth(month(r.timestamp), sum(r.HOO)) from RecordEntity r where r.chip_id=?1 and year(r.timestamp)=?2 group by month(r.timestamp) order by month(r.timestamp)")
    List<OperationHoursPerMonth> findOperationHoursPerMonth(String chipId, Integer year);

    // select new .. :
    //http://stackoverflow.com/questions/4027805/new-object-with-hql
    //http://docs.jboss.org/hibernate/core/3.5/reference/en/html/queryhql.html#queryhql-select

    //select date(timestamp) as date, sum(HOO) as count from state_record where serial_number=4 and date(timestamp)>="2014-11-16" group by date order by date desc limit 7
}
